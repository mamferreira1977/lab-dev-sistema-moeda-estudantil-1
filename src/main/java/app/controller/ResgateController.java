package app.controller;

import app.model.Usuario;
import app.repository.AlunoRepository;
import app.repository.ResgateRepository;
import app.repository.VantagemRepository;
import app.service.MoedaService;
import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resgates")
public class ResgateController {
    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final ResgateRepository resgateRepository;
    private final MoedaService moedaService;

    public ResgateController(AlunoRepository alunoRepository, VantagemRepository vantagemRepository,
                             ResgateRepository resgateRepository, MoedaService moedaService) {
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
        this.resgateRepository = resgateRepository;
        this.moedaService = moedaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("vantagens", vantagemRepository.findAll());
        model.addAttribute("resgates", resgateRepository.findAll());
        return "resgates/lista";
    }

    @PostMapping("/resgatar")
    public String resgatar(@RequestParam Long alunoId, @RequestParam Long vantagemId, RedirectAttributes ra) {
        try {
            var resgate = moedaService.resgatarVantagem(alunoId, vantagemId);
            ra.addFlashAttribute("sucesso", "Vantagem resgatada com sucesso. Código do cupom: " + resgate.getCodigo());
            return "redirect:/resgates/cupom/" + resgate.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/resgates";
        }
    }

    @GetMapping("/cupom/{id}")
    public String cupom(@PathVariable Long id, Model model) {
        model.addAttribute("resgate", resgateRepository.findById(id).orElseThrow());
        return "resgates/cupom";
    }

    @GetMapping(value = "/qrcode/{codigoVerificacao}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> qrcode(@PathVariable String codigoVerificacao) {
        var resgate = resgateRepository.findByCodigoVerificacao(codigoVerificacao).orElseThrow();
        if (resgate.getQrCodeBase64() == null || resgate.getQrCodeBase64().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Base64.getDecoder().decode(resgate.getQrCodeBase64()));
    }
}
