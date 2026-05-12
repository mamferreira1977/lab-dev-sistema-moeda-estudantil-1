package app.controller;

import app.model.EmpresaParceira;
import app.repository.EmpresaParceiraRepository;
import app.service.CadastroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresas")
public class EmpresaParceiraController {
    private final EmpresaParceiraRepository empresaRepository;
    private final CadastroService cadastroService;
    public EmpresaParceiraController(EmpresaParceiraRepository empresaRepository, CadastroService cadastroService) {
        this.empresaRepository = empresaRepository; this.cadastroService = cadastroService;
    }
    @GetMapping
    public String listar(Model model) { model.addAttribute("empresas", empresaRepository.findAll()); return "empresas/lista"; }
    @GetMapping("/nova")
    public String nova(Model model) { model.addAttribute("empresa", new EmpresaParceira()); return "empresas/form"; }
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute EmpresaParceira empresa, RedirectAttributes ra) {
        try { cadastroService.salvarEmpresa(empresa); ra.addFlashAttribute("sucesso", "Empresa parceira salva no banco de dados com sucesso."); return "redirect:/empresas"; }
        catch (Exception e) { ra.addFlashAttribute("erro", e.getMessage()); return empresa.getId()==null ? "redirect:/empresas/nova" : "redirect:/empresas/editar/"+empresa.getId(); }
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) { model.addAttribute("empresa", empresaRepository.findById(id).orElseThrow()); return "empresas/form"; }
    @GetMapping("/detalhar/{id}")
    public String detalhar(@PathVariable Long id, Model model) { model.addAttribute("empresa", empresaRepository.findById(id).orElseThrow()); return "empresas/detalhe"; }
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) { empresaRepository.deleteById(id); ra.addFlashAttribute("sucesso", "Empresa excluída com sucesso."); return "redirect:/empresas"; }
}
