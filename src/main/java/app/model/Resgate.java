package app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resgates")
public class Resgate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    @ManyToOne(optional = false)
    @JoinColumn(name = "vantagem_id")
    private Vantagem vantagem;
    @Column(nullable = false, length = 40)
    private String codigo;
    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();
    @Column(nullable = false)
    private Integer custoMoedas;

    @Column(length = 120, unique = true)
    private String codigoVerificacao;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrCodeBase64;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Vantagem getVantagem() { return vantagem; }
    public void setVantagem(Vantagem vantagem) { this.vantagem = vantagem; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Integer getCustoMoedas() { return custoMoedas; }
    public void setCustoMoedas(Integer custoMoedas) { this.custoMoedas = custoMoedas; }
    public String getCodigoVerificacao() { return codigoVerificacao; }
    public void setCodigoVerificacao(String codigoVerificacao) { this.codigoVerificacao = codigoVerificacao; }
    public String getQrCodeBase64() { return qrCodeBase64; }
    public void setQrCodeBase64(String qrCodeBase64) { this.qrCodeBase64 = qrCodeBase64; }
}
