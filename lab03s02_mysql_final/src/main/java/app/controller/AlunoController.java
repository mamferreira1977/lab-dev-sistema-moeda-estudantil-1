package app.controller;

import app.model.Aluno;
import app.repository.AlunoRepository;
import app.service.CadastroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoRepository alunoRepository;
    private final CadastroService cadastroService;
    public AlunoController(AlunoRepository alunoRepository, CadastroService cadastroService) {
        this.alunoRepository = alunoRepository; this.cadastroService = cadastroService;
    }
    @GetMapping
    public String listar(Model model) { model.addAttribute("alunos", alunoRepository.findAll()); return "alunos/lista"; }
    @GetMapping("/novo")
    public String novo(Model model) { model.addAttribute("aluno", new Aluno()); return "alunos/form"; }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Aluno aluno, RedirectAttributes ra) {
        try { cadastroService.salvarAluno(aluno); ra.addFlashAttribute("sucesso", "Aluno salvo no banco de dados com sucesso."); return "redirect:/alunos"; }
        catch (Exception e) { ra.addFlashAttribute("erro", "Erro ao salvar aluno: " + e.getMessage()); return aluno.getId()==null ? "redirect:/alunos/novo" : "redirect:/alunos/editar/"+aluno.getId(); }
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();
        if (aluno.getUsuario() != null) {
            aluno.setLogin(aluno.getUsuario().getLogin());
            aluno.setSenha("");
        }
        model.addAttribute("aluno", aluno);
        return "alunos/form";
    }
    @GetMapping("/detalhar/{id}")
    public String detalhar(@PathVariable Long id, Model model) { model.addAttribute("aluno", alunoRepository.findById(id).orElseThrow()); return "alunos/detalhe"; }
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) { alunoRepository.deleteById(id); ra.addFlashAttribute("sucesso", "Aluno excluído com sucesso."); return "redirect:/alunos"; }
}
