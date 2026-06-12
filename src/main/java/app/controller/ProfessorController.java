package app.controller;

import app.model.Professor;
import app.repository.ProfessorRepository;
import app.service.CadastroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professores")
public class ProfessorController {
    private final ProfessorRepository professorRepository;
    private final CadastroService cadastroService;

    public ProfessorController(ProfessorRepository professorRepository, CadastroService cadastroService) {
        this.professorRepository = professorRepository;
        this.cadastroService = cadastroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("professores", professorRepository.findAll());
        return "professores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Professor professor = new Professor();
        professor.setSaldoMoedas(1000);
        model.addAttribute("professor", professor);
        return "professores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Professor professor, RedirectAttributes ra) {
        try {
            cadastroService.salvarProfessor(professor);
            ra.addFlashAttribute("sucesso", "Professor salvo com sucesso.");
            return "redirect:/professores";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return professor.getId() == null ? "redirect:/professores/novo" : "redirect:/professores/editar/" + professor.getId();
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Professor professor = professorRepository.findById(id).orElseThrow();
        if (professor.getUsuario() != null) {
            professor.setLogin(professor.getUsuario().getLogin());
            professor.setSenha("");
        }
        model.addAttribute("professor", professor);
        return "professores/form";
    }

    @GetMapping("/detalhar/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        model.addAttribute("professor", professorRepository.findById(id).orElseThrow());
        return "professores/detalhe";
    }

    @GetMapping("/creditar-semestre")
    public String creditarSemestre(RedirectAttributes ra) {
        professorRepository.findAll().forEach(professor -> {
            if (professor.getSaldoMoedas() == null) professor.setSaldoMoedas(0);
            professor.setSaldoMoedas(professor.getSaldoMoedas() + 1000);
            professorRepository.save(professor);
        });
        ra.addFlashAttribute("sucesso", "Crédito semestral de 1.000 moedas aplicado aos professores. O saldo anterior foi preservado e acumulado.");
        return "redirect:/professores";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        professorRepository.deleteById(id);
        ra.addFlashAttribute("sucesso", "Professor excluído com sucesso.");
        return "redirect:/professores";
    }
}
