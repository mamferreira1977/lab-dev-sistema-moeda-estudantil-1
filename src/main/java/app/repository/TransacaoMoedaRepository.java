package app.repository;
import app.model.TransacaoMoeda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransacaoMoedaRepository extends JpaRepository<TransacaoMoeda, Long> {
    List<TransacaoMoeda> findByProfessorIdOrderByDataHoraDesc(Long professorId);
    List<TransacaoMoeda> findByAlunoIdOrderByDataHoraDesc(Long alunoId);
}
