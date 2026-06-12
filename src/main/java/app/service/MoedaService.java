package app.service;

import app.model.*;
import app.repository.*;
import app.messaging.EmailRabbitProducer;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MoedaService {
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final ResgateRepository resgateRepository;
    private final TransacaoMoedaRepository transacaoRepository;
    private final EmailRabbitProducer emailRabbitProducer;

    public MoedaService(ProfessorRepository professorRepository, AlunoRepository alunoRepository,
                        VantagemRepository vantagemRepository, ResgateRepository resgateRepository,
                        TransacaoMoedaRepository transacaoRepository, EmailRabbitProducer emailRabbitProducer) {
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
        this.resgateRepository = resgateRepository;
        this.transacaoRepository = transacaoRepository;
        this.emailRabbitProducer = emailRabbitProducer;
    }

    @Transactional
    public void enviarMoedas(Long professorId, Long alunoId, Integer quantidade, String motivo) {
        System.out.println("=== ENTROU NO MOEDA SERVICE ===");
        System.out.println("ProfessorId=" + professorId + " | AlunoId=" + alunoId + " | Quantidade=" + quantidade);

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Informe uma quantidade positiva de moedas.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("O motivo do reconhecimento é obrigatório.");
        }

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado."));
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        if (professor.getSaldoMoedas() == null) {
            professor.setSaldoMoedas(0);
        }
        if (professor.getSaldoMoedas() < quantidade) {
            throw new IllegalArgumentException("Saldo insuficiente do professor.");
        }

        // Regra de negócio:
        // O saldo do aluno deve ser sempre:
        // total de moedas recebidas - total de moedas trocadas/resgatadas.
        // Por isso o saldo do aluno é sincronizado com o histórico antes e depois da nova transação.
        sincronizarSaldoAluno(aluno);

        professor.setSaldoMoedas(professor.getSaldoMoedas() - quantidade);
        professorRepository.save(professor);

        TransacaoMoeda transacao = new TransacaoMoeda();
        transacao.setTipo(TipoTransacao.ENVIO_MOEDAS);
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        transacao.setQuantidade(quantidade);
        transacao.setMotivo(motivo.trim());
        transacaoRepository.save(transacao);

        sincronizarSaldoAluno(aluno);

        String mensagemAluno = "Olá, " + aluno.getNome() + "!\n\n"
                + "Você recebeu " + quantidade + " moeda(s) estudantis do professor " + professor.getNome() + ".\n"
                + "Motivo do reconhecimento: " + motivo.trim() + "\n\n"
                + "Saldo disponível após o crédito: " + aluno.getSaldoMoedas() + " moeda(s).\n\n"
                + "Esta é uma notificação automática do Sistema de Moeda Estudantil.";

        String mensagemProfessor = "Olá, " + professor.getNome() + "!\n\n"
                + "O envio de " + quantidade + " moeda(s) para o aluno " + aluno.getNome() + " foi realizado com sucesso.\n"
                + "Motivo informado: " + motivo.trim() + "\n\n"
                + "Saldo disponível do professor após o envio: " + professor.getSaldoMoedas() + " moeda(s).\n\n"
                + "Esta é uma notificação automática do Sistema de Moeda Estudantil.";

        System.out.println("=== CHAMANDO PRODUCER RABBITMQ - ALUNO ===");
        emailRabbitProducer.publicarEmail(aluno.getEmail(), "Moeda Estudantil - você recebeu " + quantidade + " moeda(s)", mensagemAluno);

        System.out.println("=== CHAMANDO PRODUCER RABBITMQ - PROFESSOR ===");
        emailRabbitProducer.publicarEmail(professor.getEmail(), "Moeda Estudantil - confirmação de envio de " + quantidade + " moeda(s)", mensagemProfessor);

        System.out.println("=== FIM DO ENVIO DE MOEDAS COM RABBITMQ ===");
    }

    @Transactional
    public Resgate resgatarVantagem(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));

        if (vantagem.getCustoMoedas() == null || vantagem.getCustoMoedas() <= 0) {
            throw new IllegalArgumentException("Custo da vantagem inválido.");
        }

        // Antes do resgate, corrige o saldo com base nas transações existentes.
        sincronizarSaldoAluno(aluno);

        if (aluno.getSaldoMoedas() < vantagem.getCustoMoedas()) {
            throw new IllegalArgumentException("Saldo insuficiente para resgatar esta vantagem.");
        }

        String codigo = "CUPOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Resgate resgate = new Resgate();
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);
        resgate.setCodigo(codigo);
        resgate.setCustoMoedas(vantagem.getCustoMoedas());
        resgateRepository.save(resgate);

        TransacaoMoeda transacao = new TransacaoMoeda();
        transacao.setTipo(TipoTransacao.RESGATE_VANTAGEM);
        transacao.setAluno(aluno);
        transacao.setVantagem(vantagem);
        transacao.setQuantidade(vantagem.getCustoMoedas());
        transacao.setMotivo("Resgate da vantagem: " + vantagem.getTitulo() + " | Código: " + codigo);
        transacaoRepository.save(transacao);

        // Depois do resgate, o saldo passa a ser:
        // total recebido - total resgatado.
        sincronizarSaldoAluno(aluno);

        String mensagemAluno = "Cupom gerado para a vantagem '" + vantagem.getTitulo() + "'. Código: " + codigo
                + "\n\nSaldo disponível após o resgate: " + aluno.getSaldoMoedas() + " moeda(s).";
        String mensagemEmpresa = "O aluno " + aluno.getNome() + " resgatou a vantagem '" + vantagem.getTitulo()
                + "'. Código para conferência: " + codigo;

        emailRabbitProducer.publicarEmail(aluno.getEmail(), "Cupom de resgate - Moeda Estudantil", mensagemAluno);
        emailRabbitProducer.publicarEmail(vantagem.getEmpresa().getEmail(), "Conferência de cupom - Moeda Estudantil", mensagemEmpresa);

        return resgate;
    }

    @Transactional
    public int sincronizarSaldoAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        return sincronizarSaldoAluno(aluno);
    }

    private int sincronizarSaldoAluno(Aluno aluno) {
        List<TransacaoMoeda> transacoes = transacaoRepository.findByAlunoIdOrderByDataHoraDesc(aluno.getId());

        int totalRecebido = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.ENVIO_MOEDAS || t.getTipo() == TipoTransacao.RECEBIMENTO_MOEDAS)
                .mapToInt(t -> t.getQuantidade() == null ? 0 : t.getQuantidade())
                .sum();

        int totalResgatado = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RESGATE_VANTAGEM)
                .mapToInt(t -> t.getQuantidade() == null ? 0 : t.getQuantidade())
                .sum();

        int saldoCorreto = totalRecebido - totalResgatado;
        if (saldoCorreto < 0) {
            saldoCorreto = 0;
        }

        aluno.setSaldoMoedas(saldoCorreto);
        alunoRepository.save(aluno);

        System.out.println("=== SALDO DO ALUNO SINCRONIZADO ===");
        System.out.println("AlunoId=" + aluno.getId()
                + " | Recebido=" + totalRecebido
                + " | Resgatado=" + totalResgatado
                + " | Saldo=" + saldoCorreto);

        return saldoCorreto;
    }
}
