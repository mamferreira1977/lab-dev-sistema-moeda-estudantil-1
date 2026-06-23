package app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails_simulados")
public class EmailSimulado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 160)
    private String remetente;

    @Column(nullable = false, length = 160)
    private String destinatario;

    @Column(nullable = false, length = 160)
    private String assunto;

    @Column(nullable = false, length = 2000)
    private String mensagem;

    @Column(name = "status_envio", nullable = false, length = 40)
    private String statusEnvio = "REGISTRADO";

    @Column(name = "erro_envio", length = 1000)
    private String erroEnvio;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRemetente() { return remetente; }
    public void setRemetente(String remetente) { this.remetente = remetente; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getStatusEnvio() { return statusEnvio; }
    public void setStatusEnvio(String statusEnvio) { this.statusEnvio = statusEnvio; }
    public String getErroEnvio() { return erroEnvio; }
    public void setErroEnvio(String erroEnvio) { this.erroEnvio = erroEnvio; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
