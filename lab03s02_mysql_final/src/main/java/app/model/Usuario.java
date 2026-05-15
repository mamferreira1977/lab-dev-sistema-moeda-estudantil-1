package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String login;
    @Column(nullable = false, length = 100)
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil;
    @Column(nullable = false)
    private Boolean ativo = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getNomeExibicao() {
        if (this.login == null || this.login.isBlank()) {
            return "Administrador";
        }
        String valor = this.login;
        int arroba = valor.indexOf("@");
        if (arroba > 0) {
            valor = valor.substring(0, arroba);
        }
        valor = valor.replace(".", " ").replace("_", " ").replace("-", " ").trim();
        if (valor.isBlank()) {
            return this.login;
        }
        String[] partes = valor.split("\\s+");
        StringBuilder nome = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isBlank()) {
                nome.append(Character.toUpperCase(parte.charAt(0)));
                if (parte.length() > 1) {
                    nome.append(parte.substring(1).toLowerCase());
                }
                nome.append(" ");
            }
        }
        return nome.toString().trim();
    }

}
