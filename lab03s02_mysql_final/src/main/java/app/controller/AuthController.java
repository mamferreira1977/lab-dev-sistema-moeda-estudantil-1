package app.controller;

import app.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    public AuthController(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }

    @GetMapping({"/", "/login"})
    public String login() { return "auth/login"; }

    @PostMapping("/login")
    public String autenticar(@RequestParam String login, @RequestParam String senha, HttpSession session, Model model) {
        return usuarioRepository.findByLoginAndSenhaAndAtivoTrue(login, senha)
                .map(usuario -> { session.setAttribute("usuarioLogado", usuario); return "redirect:/home"; })
                .orElseGet(() -> { model.addAttribute("erro", "Login ou senha inválidos."); return "auth/login"; });
    }

    @GetMapping("/home")
    public String home() { return "auth/home"; }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
}
