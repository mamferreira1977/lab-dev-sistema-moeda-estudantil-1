package app.repository;
import app.model.Aluno;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByUsuarioId(Long usuarioId);
}
