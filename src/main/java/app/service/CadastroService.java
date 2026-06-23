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
    private final ProfessorRepository professorRepository;

    public CadastroService(AlunoRepository alunoRepository, EmpresaParceiraRepository empresaRepository, UsuarioRepository usuarioRepository, ProfessorRepository professorRepository) {
        this.alunoRepository = alunoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional
    public Aluno salvarAluno(Aluno dadosFormulario) {
        Aluno aluno;

        if (dadosFormulario.getId() != null) {
            aluno = alunoRepository.findById(dadosFormulario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado para edição."));
            aluno.setNome(dadosFormulario.getNome());
            aluno.setEmail(dadosFormulario.getEmail());
            aluno.setCpf(dadosFormulario.getCpf());
            aluno.setRg(dadosFormulario.getRg());
            aluno.setEndereco(dadosFormulario.getEndereco());
            aluno.setInstituicaoEnsino(dadosFormulario.getInstituicaoEnsino());
            aluno.setCurso(dadosFormulario.getCurso());
            aluno.setSaldoMoedas(dadosFormulario.getSaldoMoedas() == null ? aluno.getSaldoMoedas() : dadosFormulario.getSaldoMoedas());
        } else {
            aluno = dadosFormulario;
        }

        Usuario usuario = aluno.getUsuario();
        if (usuario == null) {
            usuario = new Usuario();
        }

        String loginAluno = dadosFormulario.getLogin();
        if (loginAluno == null || loginAluno.isBlank()) {
            loginAluno = dadosFormulario.getEmail();
        }
        if (loginAluno == null || loginAluno.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório para criar o login do aluno.");
        }
        loginAluno = loginAluno.trim();

        Long idUsuarioAtual = usuario.getId();
        boolean loginPertenceAOutroUsuario = usuarioRepository.findByLogin(loginAluno)
                .map(u -> idUsuarioAtual == null || !u.getId().equals(idUsuarioAtual))
                .orElse(false);
        if (loginPertenceAOutroUsuario) {
            throw new IllegalArgumentException("Login já cadastrado. Escolha outro login.");
        }

        usuario.setLogin(loginAluno);
        if (dadosFormulario.getSenha() != null && !dadosFormulario.getSenha().isBlank()) {
            usuario.setSenha(dadosFormulario.getSenha());
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
    public EmpresaParceira salvarEmpresa(EmpresaParceira dadosFormulario) {
        EmpresaParceira empresa;

        if (dadosFormulario.getId() != null) {
            empresa = empresaRepository.findById(dadosFormulario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa parceira não encontrada para edição."));
            empresa.setRazaoSocial(dadosFormulario.getRazaoSocial());
            empresa.setNomeFantasia(dadosFormulario.getNomeFantasia());
            empresa.setCnpj(dadosFormulario.getCnpj());
            empresa.setEmail(dadosFormulario.getEmail());
            empresa.setTelefone(dadosFormulario.getTelefone());
            empresa.setEndereco(dadosFormulario.getEndereco());
            empresa.setDescricaoVantagem(dadosFormulario.getDescricaoVantagem());
            empresa.setCustoMoedas(dadosFormulario.getCustoMoedas());
        } else {
            empresa = dadosFormulario;
        }

        if (empresa.getEmail() == null || empresa.getEmail().isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        final Long idEmpresaAtual = empresa.getId();
        String cnpjInformado = empresa.getCnpj();
        if (cnpjInformado != null && !cnpjInformado.isBlank()) {
            boolean cnpjPertenceAOutraEmpresa = empresaRepository.findAll().stream().anyMatch(e ->
                    e.getId() != null
                            && !e.getId().equals(idEmpresaAtual)
                            && e.getCnpj() != null
                            && e.getCnpj().equals(cnpjInformado));
            if (cnpjPertenceAOutraEmpresa) {
                throw new IllegalArgumentException("Já existe uma empresa cadastrada com este CNPJ.");
            }
        }

        String emailInformado = empresa.getEmail().trim();
        boolean emailPertenceAOutraEmpresa = empresaRepository.findAll().stream().anyMatch(e ->
                e.getId() != null
                        && !e.getId().equals(idEmpresaAtual)
                        && e.getEmail() != null
                        && e.getEmail().equalsIgnoreCase(emailInformado));
        if (emailPertenceAOutraEmpresa) {
            throw new IllegalArgumentException("Já existe uma empresa cadastrada com este e-mail.");
        }
        empresa.setEmail(emailInformado);

        Usuario usuario = empresa.getUsuario();
        if (usuario == null) {
            usuario = new Usuario();
        }

        String loginEmpresa = dadosFormulario.getLogin();
        if (loginEmpresa == null || loginEmpresa.isBlank()) {
            loginEmpresa = dadosFormulario.getEmail();
        }
        if (loginEmpresa == null || loginEmpresa.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório para criar o login da empresa.");
        }
        loginEmpresa = loginEmpresa.trim();

        Long idUsuarioAtual = usuario.getId();
        boolean loginPertenceAOutroUsuario = usuarioRepository.findByLogin(loginEmpresa)
                .map(u -> idUsuarioAtual == null || !u.getId().equals(idUsuarioAtual))
                .orElse(false);
        if (loginPertenceAOutroUsuario) {
            throw new IllegalArgumentException("Login já cadastrado. Escolha outro login.");
        }

        usuario.setLogin(loginEmpresa);
        if (dadosFormulario.getSenha() != null && !dadosFormulario.getSenha().isBlank()) {
            usuario.setSenha(dadosFormulario.getSenha());
        } else if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            usuario.setSenha("123456");
        }
        usuario.setPerfil(Perfil.EMPRESA);
        usuario.setAtivo(true);
        empresa.setUsuario(usuario);

        return empresaRepository.save(empresa);
    }

    @Transactional
    public Professor salvarProfessor(Professor dadosFormulario) {
        Professor professor;

        if (dadosFormulario.getId() != null) {
            professor = professorRepository.findById(dadosFormulario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado para edição."));
            professor.setNome(dadosFormulario.getNome());
            professor.setCpf(dadosFormulario.getCpf());
            professor.setDepartamento(dadosFormulario.getDepartamento());
            professor.setEmail(dadosFormulario.getEmail());
            professor.setInstituicao(dadosFormulario.getInstituicao());
            professor.setInstituicaoId(1);
            professor.setInstituicaoNome(dadosFormulario.getInstituicao());
            professor.setSaldoMoedas(dadosFormulario.getSaldoMoedas() == null ? professor.getSaldoMoedas() : dadosFormulario.getSaldoMoedas());
        } else {
            professor = dadosFormulario;
        }

        if (professor.getNome() == null || professor.getNome().isBlank()) throw new IllegalArgumentException("O nome do professor é obrigatório.");
        if (professor.getCpf() == null || professor.getCpf().isBlank()) throw new IllegalArgumentException("O CPF do professor é obrigatório.");
        if (professor.getEmail() == null || professor.getEmail().isBlank()) throw new IllegalArgumentException("O e-mail do professor é obrigatório.");
        if (professor.getDepartamento() == null || professor.getDepartamento().isBlank()) throw new IllegalArgumentException("O departamento é obrigatório.");
        if (professor.getInstituicao() == null || professor.getInstituicao().isBlank()) throw new IllegalArgumentException("A instituição é obrigatória.");

        professor.setNome(professor.getNome().trim());
        professor.setCpf(professor.getCpf().trim());
        professor.setEmail(professor.getEmail().trim());
        professor.setDepartamento(professor.getDepartamento().trim());
        professor.setInstituicaoId(1);
        professor.setInstituicaoNome(professor.getInstituicao().trim());

        final Long idProfessorAtual = professor.getId();
        boolean cpfPertenceAOutroProfessor = professorRepository.findByCpf(professor.getCpf())
                .map(p -> idProfessorAtual == null || !p.getId().equals(idProfessorAtual))
                .orElse(false);
        if (cpfPertenceAOutroProfessor) throw new IllegalArgumentException("Já existe professor cadastrado com este CPF.");

        boolean emailPertenceAOutroProfessor = professorRepository.findByEmailIgnoreCase(professor.getEmail())
                .map(p -> idProfessorAtual == null || !p.getId().equals(idProfessorAtual))
                .orElse(false);
        if (emailPertenceAOutroProfessor) throw new IllegalArgumentException("Já existe professor cadastrado com este e-mail.");

        Usuario usuario = professor.getUsuario();
        if (usuario == null) usuario = new Usuario();

        String loginProfessor = dadosFormulario.getLogin();
        if (loginProfessor == null || loginProfessor.isBlank()) loginProfessor = professor.getEmail();
        loginProfessor = loginProfessor.trim();

        Long idUsuarioAtual = usuario.getId();
        boolean loginPertenceAOutroUsuario = usuarioRepository.findByLogin(loginProfessor)
                .map(u -> idUsuarioAtual == null || !u.getId().equals(idUsuarioAtual))
                .orElse(false);
        if (loginPertenceAOutroUsuario) throw new IllegalArgumentException("Login já cadastrado. Escolha outro login.");

        usuario.setLogin(loginProfessor);
        if (dadosFormulario.getSenha() != null && !dadosFormulario.getSenha().isBlank()) usuario.setSenha(dadosFormulario.getSenha());
        else if (usuario.getSenha() == null || usuario.getSenha().isBlank()) usuario.setSenha("123456");
        usuario.setPerfil(Perfil.PROFESSOR);
        usuario.setAtivo(true);
        professor.setUsuario(usuario);
        if (professor.getSaldoMoedas() == null) professor.setSaldoMoedas(1000);
        return professorRepository.save(professor);
    }

}
