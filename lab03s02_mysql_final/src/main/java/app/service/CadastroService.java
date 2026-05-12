package app.service;

import app.model.*;
import app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {
    private final AlunoRepository alunoRepository;
    private final EmpresaParceiraRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public CadastroService(AlunoRepository alunoRepository, EmpresaParceiraRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.alunoRepository = alunoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Aluno salvarAluno(Aluno aluno) {
        Usuario usuario = aluno.getUsuario();
        if (usuario == null) {
            usuario = new Usuario();
        }
        if (aluno.getId() == null && usuarioRepository.existsByLogin(aluno.getLogin())) {
            throw new IllegalArgumentException("Login já cadastrado. Escolha outro login.");
        }
        String loginAluno = aluno.getLogin();
        if (loginAluno == null || loginAluno.isBlank()) {
            loginAluno = aluno.getEmail();
        }
        if (loginAluno == null || loginAluno.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório para criar o login do aluno.");
        }
        usuario.setLogin(loginAluno);

        if (aluno.getSenha() != null && !aluno.getSenha().isBlank()) {
            usuario.setSenha(aluno.getSenha());
        } else if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            usuario.setSenha("123456");
        }
        usuario.setPerfil(Perfil.ALUNO);
        usuario.setAtivo(true);
        aluno.setUsuario(usuario);
        if (aluno.getSaldoMoedas() == null) aluno.setSaldoMoedas(0);
        return alunoRepository.save(aluno);
    }

    @Transactional
    public EmpresaParceira salvarEmpresa(EmpresaParceira empresa) {
        Usuario usuario = empresa.getUsuario();
        if (usuario == null) {
            usuario = new Usuario();
        }
        if (empresa.getId() == null && usuarioRepository.existsByLogin(empresa.getLogin())) {
            throw new IllegalArgumentException("Login já cadastrado. Escolha outro login.");
        }
        if (empresaRepository.findAll().stream().anyMatch(e ->
                !e.getId().equals(empresa.getId() == null ? -1L : empresa.getId())
                && empresa.getCnpj() != null && !empresa.getCnpj().isBlank()
                && e.getCnpj() != null && empresa.getCnpj() != null && !empresa.getCnpj().isBlank() && e.getCnpj() != null && e.getCnpj().equals(empresa.getCnpj()))) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este CNPJ.");
        }

        if (empresaRepository.findAll().stream().anyMatch(e ->
                !e.getId().equals(empresa.getId() == null ? -1L : empresa.getId())
                && empresa.getEmail() != null && !empresa.getEmail().isBlank()
                && e.getEmail() != null && empresa.getEmail() != null && !empresa.getEmail().isBlank() && e.getEmail() != null && e.getEmail().equalsIgnoreCase(empresa.getEmail()))) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este e-mail.");
        }

        String loginEmpresa = empresa.getLogin();
        if (loginEmpresa == null || loginEmpresa.isBlank()) {
            loginEmpresa = empresa.getEmail();
        }
        if (loginEmpresa == null || loginEmpresa.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório para criar o login da empresa.");
        }
        usuario.setLogin(loginEmpresa);

        if (empresa.getSenha() != null && !empresa.getSenha().isBlank()) {
            usuario.setSenha(empresa.getSenha());
        } else if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            usuario.setSenha("123456");
        }

        usuario.setPerfil(Perfil.EMPRESA);
        usuario.setAtivo(true);
        empresa.setUsuario(usuario);

        return empresaRepository.save(empresa);
    }
}
