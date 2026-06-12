package app.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class BancoCompatibilidadeService implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;

    public BancoCompatibilidadeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Ajustes defensivos para bancos criados em versões anteriores do projeto.
        executar("ALTER TABLE professores ADD COLUMN email VARCHAR(120) NULL");
        executar("ALTER TABLE professores ADD COLUMN instituicao VARCHAR(120) NOT NULL DEFAULT 'PUC Minas'");
        executar("UPDATE professores SET instituicao = 'PUC Minas' WHERE instituicao IS NULL OR instituicao = ''");
        executar("ALTER TABLE professores MODIFY COLUMN instituicao VARCHAR(120) NOT NULL DEFAULT 'PUC Minas'");
        executar("ALTER TABLE professores ADD COLUMN instituicao_id INT NOT NULL DEFAULT 1");
        executar("ALTER TABLE professores MODIFY COLUMN instituicao_id INT NOT NULL DEFAULT 1");
        executar("ALTER TABLE professores ADD COLUMN instituicao_nome VARCHAR(120) NULL");
        executar("UPDATE professores SET instituicao_id = 1 WHERE instituicao_id IS NULL");
        executar("UPDATE professores SET instituicao_nome = 'PUC Minas' WHERE instituicao_nome IS NULL OR instituicao_nome = ''");
        executar("ALTER TABLE professores MODIFY COLUMN instituicao_nome VARCHAR(120) NOT NULL DEFAULT 'PUC Minas'");
    }

    private void executar(String sql) {
        try { jdbcTemplate.execute(sql); } catch (Exception ignored) { }
    }
}
