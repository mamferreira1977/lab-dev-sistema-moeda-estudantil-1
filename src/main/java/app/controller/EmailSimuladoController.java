package app.controller;

import app.repository.EmailSimuladoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/emails-simulados")
public class EmailSimuladoController {
    private final EmailSimuladoRepository emailRepository;
    public EmailSimuladoController(EmailSimuladoRepository emailRepository) { this.emailRepository = emailRepository; }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emails", emailRepository.findAll());
        return "emails/lista";
    }
}
