package app.controller;

import app.model.Usuario;
import app.repository.AlunoRepository;
import app.repository.ProfessorRepository;
import app.repository.TransacaoMoedaRepository;
import app.service.MoedaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/moedas")
public class MoedaController {
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TransacaoMoedaRepository transacaoRepository;
    private final MoedaService moedaService;

    public MoedaController(ProfessorRepository professorRepository, AlunoRepository alunoRepository,
                           TransacaoMoedaRepository transacaoRepository, MoedaService moedaService) {
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.transacaoRepository = transacaoRepository;
        this.moedaService = moedaService;
    }

    @GetMapping("/enviar")
    public String formularioEnvio(Model model, HttpSession session) {
        model.addAttribute("professores", professorRepository.findAll());
        model.addAttribute("alunos", alunoRepository.findAll());
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario != null) professorRepository.findByUsuarioId(usuario.getId()).ifPresent(p -> model.addAttribute("professorLogado", p));
        return "moedas/enviar";
    }

    @PostMapping({"/enviar", "/enviar-moedas"})
    public String enviar(@RequestParam Long professorId, @RequestParam Long alunoId,
                         @RequestParam Integer quantidade, @RequestParam String motivo,
                         RedirectAttributes ra) {
        System.out.println("=== ENTROU NO CONTROLLER ENVIAR MOEDAS ===");
        System.out.println("ProfessorId=" + professorId + " | AlunoId=" + alunoId + " | Quantidade=" + quantidade);

        try {
            moedaService.enviarMoedas(professorId, alunoId, quantidade, motivo);
            ra.addFlashAttribute("sucesso", "Moedas enviadas com sucesso. Professor e aluno foram notificados automaticamente via RabbitMQ.");
        } catch (Exception e) {
            System.out.println("=== ERRO NO CONTROLLER ENVIAR MOEDAS === " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/moedas/enviar";
    }

    @GetMapping("/teste-rabbit")
    public String testeRabbit(RedirectAttributes ra) {
        System.out.println("=== TESTE RABBITMQ VIA CONTROLLER ===");
        try {
            // Usa os primeiros cadastros existentes para testar o fluxo completo do serviço.
            var professor = professorRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cadastre ao menos um professor para testar o RabbitMQ."));
            var aluno = alunoRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cadastre ao menos um aluno para testar o RabbitMQ."));

            if (professor.getSaldoMoedas() == null || professor.getSaldoMoedas() < 1) {
                professor.setSaldoMoedas(1);
                professorRepository.save(professor);
            }
            moedaService.enviarMoedas(professor.getId(), aluno.getId(), 1, "Teste automático RabbitMQ");
            ra.addFlashAttribute("sucesso", "Teste RabbitMQ executado. Verifique a fila e a tela de e-mails.");
        } catch (Exception e) {
            System.out.println("=== ERRO NO TESTE RABBITMQ === " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("erro", "Erro no teste RabbitMQ: " + e.getMessage());
        }
        return "redirect:/moedas/enviar";
    }


    @GetMapping("/meu-extrato")
    public String meuExtrato(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }
        return alunoRepository.findByUsuarioId(usuario.getId())
                .map(aluno -> {
                    carregarExtratoAluno(model, aluno.getId());
                    return "extratos/aluno";
                })
                .orElse("redirect:/home");
    }

    @GetMapping("/extrato-professor/{id}")
    public String extratoProfessor(@PathVariable Long id, Model model) {
        model.addAttribute("professor", professorRepository.findById(id).orElseThrow());
        model.addAttribute("transacoes", transacaoRepository.findByProfessorIdOrderByDataHoraDesc(id));
        return "extratos/professor";
    }

    @GetMapping("/extrato-aluno/{id}")
    public String extratoAluno(@PathVariable Long id, Model model) {
        carregarExtratoAluno(model, id);
        return "extratos/aluno";
    }

    private void carregarExtratoAluno(Model model, Long alunoId) {
        // Antes de exibir o extrato, sincroniza o saldo do aluno com o histórico:
        // saldo = total recebido - total trocado/resgatado.
        moedaService.sincronizarSaldoAluno(alunoId);

        var aluno = alunoRepository.findById(alunoId).orElseThrow();
        var transacoes = transacaoRepository.findByAlunoIdOrderByDataHoraDesc(alunoId);

        int totalRecebido = transacoes.stream()
                .filter(t -> t.getTipo() == app.model.TipoTransacao.ENVIO_MOEDAS
                        || t.getTipo() == app.model.TipoTransacao.RECEBIMENTO_MOEDAS)
                .mapToInt(t -> t.getQuantidade() == null ? 0 : t.getQuantidade())
                .sum();

        int totalTrocado = transacoes.stream()
                .filter(t -> t.getTipo() == app.model.TipoTransacao.RESGATE_VANTAGEM)
                .mapToInt(t -> t.getQuantidade() == null ? 0 : t.getQuantidade())
                .sum();

        int saldoCorreto = totalRecebido - totalTrocado;
        if (saldoCorreto < 0) {
            saldoCorreto = 0;
        }

        model.addAttribute("aluno", aluno);
        model.addAttribute("transacoes", transacoes);
        model.addAttribute("totalRecebido", totalRecebido);
        model.addAttribute("totalTrocado", totalTrocado);
        model.addAttribute("saldoCorreto", saldoCorreto);
    }
}
