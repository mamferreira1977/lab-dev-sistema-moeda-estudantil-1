package app.repository;

import app.model.Professor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsuarioId(Long usuarioId);
    Optional<Professor> findByCpf(String cpf);
    Optional<Professor> findByEmailIgnoreCase(String email);
}
