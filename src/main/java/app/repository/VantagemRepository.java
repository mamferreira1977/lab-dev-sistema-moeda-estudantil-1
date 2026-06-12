package app.repository;
import app.model.Vantagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    List<Vantagem> findByEmpresaId(Long empresaId);
}
