package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professores")
public class Professor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String cpf;

    @Column(unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String departamento;

    /*
       Compatibilidade com o banco já criado na máquina da aluna:
       versões anteriores criaram a coluna instituicao_id como INT e NOT NULL.
       Por isso o sistema NÃO grava mais "PUCMINAS" nessa coluna. Ele grava o id 1
       e guarda o texto informado no campo instituicao_nome.
    */
    @Column(name = "instituicao_id", nullable = false)
    private Integer instituicaoId = 1;

    @Column(name = "instituicao_nome", length = 120)
    private String instituicaoNome;

    // Coluna legada existente em alguns bancos criados nas versões anteriores.
    // Mantida para evitar erro: Field 'instituicao' doesn't have a default value.
    @Column(name = "instituicao", nullable = false, length = 120)
    private String instituicao = "PUC Minas";

    @Column(name = "saldo_moedas", nullable = false)
    private Integer saldoMoedas = 1000;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Transient private String login;
    @Transient private String senha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public Integer getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(Integer instituicaoId) { this.instituicaoId = instituicaoId == null ? 1 : instituicaoId; }
    public String getInstituicaoNome() { return instituicaoNome; }
    public void setInstituicaoNome(String instituicaoNome) { this.instituicaoNome = instituicaoNome; }
    public String getInstituicao() { return instituicao != null && !instituicao.isBlank() ? instituicao : instituicaoNome; }
    public void setInstituicao(String instituicao) {
        this.instituicao = (instituicao == null || instituicao.isBlank()) ? "PUC Minas" : instituicao.trim();
        this.instituicaoNome = this.instituicao;
        this.instituicaoId = 1;
    }
    public Integer getSaldoMoedas() { return saldoMoedas; }
    public void setSaldoMoedas(Integer saldoMoedas) { this.saldoMoedas = saldoMoedas; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
