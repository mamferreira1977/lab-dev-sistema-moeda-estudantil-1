package app.controller;

import app.model.Perfil;
import app.model.Usuario;
import app.repository.AlunoRepository;
import app.repository.ProfessorRepository;
import app.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    public AuthController(UsuarioRepository usuarioRepository,
                          AlunoRepository alunoRepository,
                          ProfessorRepository professorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }

    @GetMapping({"/", "/login"})
    public String login() { return "auth/login"; }

    @PostMapping("/login")
    public String autenticar(@RequestParam String login, @RequestParam String senha, HttpSession session, Model model) {
        return usuarioRepository.findByLoginAndSenhaAndAtivoTrue(login, senha)
                .map(usuario -> {
                    session.setAttribute("usuarioLogado", usuario);

                    // Lab04S01: ao entrar no sistema, o aluno já visualiza o seu extrato,
                    // com saldo atual e histórico de moedas recebidas/trocadas.
                    if (usuario.getPerfil() == Perfil.ALUNO) {
                        return alunoRepository.findByUsuarioId(usuario.getId())
                                .map(aluno -> "redirect:/moedas/extrato-aluno/" + aluno.getId())
                                .orElse("redirect:/home");
                    }

                    // Para professor, mantém o acesso ao painel e permite envio de moedas.
                    if (usuario.getPerfil() == Perfil.PROFESSOR) {
                        return professorRepository.findByUsuarioId(usuario.getId())
                                .map(professor -> "redirect:/moedas/enviar")
                                .orElse("redirect:/home");
                    }

                    return "redirect:/home";
                })
                .orElseGet(() -> { model.addAttribute("erro", "Login ou senha inválidos."); return "auth/login"; });
    }

    @GetMapping("/home")
    public String home() { return "auth/home"; }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
}
