package app.repository;
import app.model.EmpresaParceira;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    Optional<EmpresaParceira> findByUsuarioId(Long usuarioId);
}
