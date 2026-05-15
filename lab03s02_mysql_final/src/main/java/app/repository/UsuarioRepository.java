package app.repository;

import app.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLoginAndSenhaAndAtivoTrue(String login, String senha);
    boolean existsByLogin(String login);
    Optional<Usuario> findByLogin(String login);
}
