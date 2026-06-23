package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empresas_parceiras")
public class EmpresaParceira {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razao_social", length = 150)
    private String razaoSocial;
    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;
    @Column(unique = true, length = 30)
    private String cnpj;
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(length = 30)
    private String telefone;
    @Column(length = 180)
    private String endereco;
    @Column(name = "descricao_vantagem", length = 255)
    private String descricaoVantagem;
    @Column(name = "custo_moedas")
    private Integer custoMoedas;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Transient private String login;
    @Transient private String senha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getDescricaoVantagem() { return descricaoVantagem; }
    public void setDescricaoVantagem(String descricaoVantagem) { this.descricaoVantagem = descricaoVantagem; }
    public Integer getCustoMoedas() { return custoMoedas; }
    public void setCustoMoedas(Integer custoMoedas) { this.custoMoedas = custoMoedas; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
