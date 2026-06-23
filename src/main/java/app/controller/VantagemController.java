package app.controller;

import app.model.Vantagem;
import app.repository.EmpresaParceiraRepository;
import app.repository.VantagemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vantagens")
public class VantagemController {
    private final VantagemRepository vantagemRepository;
    private final EmpresaParceiraRepository empresaRepository;

    public VantagemController(VantagemRepository vantagemRepository, EmpresaParceiraRepository empresaRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vantagens", vantagemRepository.findAll());
        return "vantagens/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("vantagem", new Vantagem());
        model.addAttribute("empresas", empresaRepository.findAll());
        return "vantagens/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Vantagem vantagem, @RequestParam Long empresaId, RedirectAttributes ra) {
        try {
            if (vantagem.getTitulo() == null || vantagem.getTitulo().isBlank()) throw new IllegalArgumentException("O título da vantagem é obrigatório.");
            if (vantagem.getDescricao() == null || vantagem.getDescricao().isBlank()) throw new IllegalArgumentException("A descrição da vantagem é obrigatória.");
            if (vantagem.getCustoMoedas() == null || vantagem.getCustoMoedas() <= 0) throw new IllegalArgumentException("O custo em moedas deve ser positivo.");
            vantagem.setEmpresa(empresaRepository.findById(empresaId).orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada.")));
            vantagemRepository.save(vantagem);
            ra.addFlashAttribute("sucesso", "Vantagem salva com sucesso.");
            return "redirect:/vantagens";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return vantagem.getId() == null ? "redirect:/vantagens/nova" : "redirect:/vantagens/editar/" + vantagem.getId();
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("vantagem", vantagemRepository.findById(id).orElseThrow());
        model.addAttribute("empresas", empresaRepository.findAll());
        return "vantagens/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        vantagemRepository.deleteById(id);
        ra.addFlashAttribute("sucesso", "Vantagem excluída com sucesso.");
        return "redirect:/vantagens";
    }
}
