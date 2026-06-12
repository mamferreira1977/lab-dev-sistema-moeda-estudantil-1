package app.repository;
import app.model.Resgate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ResgateRepository extends JpaRepository<Resgate, Long> {
    List<Resgate> findByAlunoIdOrderByDataHoraDesc(Long alunoId);
}
