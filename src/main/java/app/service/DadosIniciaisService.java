package app.service;

import app.model.Aluno;
import app.model.Perfil;
import app.model.Professor;
import app.model.Usuario;
import app.repository.AlunoRepository;
import app.repository.ProfessorRepository;
import app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class DadosIniciaisService implements CommandLineRunner {
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;

    public DadosIniciaisService(ProfessorRepository professorRepository, AlunoRepository alunoRepository, UsuarioRepository usuarioRepository) {
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (professorRepository.count() == 0 && !usuarioRepository.existsByLogin("professor@pucminas.br")) {
            criarProfessor("Professor Exemplo", "00000000000", "professor@pucminas.br", "Engenharia de Software", "PUC Minas", "professor@pucminas.br", "123456");
        }
        if (alunoRepository.count() == 0 && !usuarioRepository.existsByLogin("aluno@pucminas.br")) {
            criarAluno("Aluno Exemplo", "aluno@pucminas.br", "11111111111", "MG-000000", "Belo Horizonte/MG", "PUC Minas", "Engenharia de Software", "aluno@pucminas.br", "123456");
        }
    }

    private void criarProfessor(String nome, String cpf, String email, String departamento, String instituicao, String login, String senha) {
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setPerfil(Perfil.PROFESSOR);
        usuario.setAtivo(true);

        Professor professor = new Professor();
        professor.setNome(nome);
        professor.setCpf(cpf);
        professor.setEmail(email);
        professor.setDepartamento(departamento);
        professor.setInstituicao(instituicao);
        professor.setSaldoMoedas(1000);
        professor.setUsuario(usuario);
        professorRepository.save(professor);
    }

    private void criarAluno(String nome, String email, String cpf, String rg, String endereco, String instituicao, String curso, String login, String senha) {
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setPerfil(Perfil.ALUNO);
        usuario.setAtivo(true);

        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setEmail(email);
        aluno.setCpf(cpf);
        aluno.setRg(rg);
        aluno.setEndereco(endereco);
        aluno.setInstituicaoEnsino(instituicao);
        aluno.setCurso(curso);
        aluno.setSaldoMoedas(0);
        aluno.setUsuario(usuario);
        alunoRepository.save(aluno);
    }
}
