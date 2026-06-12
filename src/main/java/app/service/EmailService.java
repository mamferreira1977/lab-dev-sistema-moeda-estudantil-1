package app.service;

import app.model.EmailSimulado;
import app.repository.EmailSimuladoRepository;
import java.util.Locale;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final EmailSimuladoRepository repository;

    @Value("${spring.mail.host:${MAIL_HOST:auto}}")
    private String hostConfigurado;

    @Value("${spring.mail.port:${MAIL_PORT:587}}")
    private Integer porta;

    @Value("${spring.mail.username:${MAIL_USERNAME:}}")
    private String usuarioSmtp;

    @Value("${spring.mail.password:${MAIL_PASSWORD:}}")
    private String senhaSmtp;

    @Value("${app.email.remetente:${MAIL_FROM:${spring.mail.username:${MAIL_USERNAME:}}}}")
    private String remetente;

    @Value("${app.email.envio-real:${APP_EMAIL_ENVIO_REAL:true}}")
    private boolean envioRealHabilitado;

    @Value("${app.email.exigir-envio-real:${APP_EMAIL_EXIGIR_ENVIO_REAL:false}}")
    private boolean exigirEnvioReal;

    @Value("${app.email.nome-remetente:Sistema de Moeda Estudantil}")
    private String nomeRemetente;

    public EmailService(EmailSimuladoRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra a mensagem no banco e, se houver SMTP remetente configurado, dispara o e-mail real.
     * O destinatário pode ser Gmail, Hotmail, Outlook, Yahoo, institucional etc.; quem precisa de
     * autenticação SMTP é apenas a conta REMETENTE do sistema.
     */
    public EmailSimulado enviar(String destinatario, String assunto, String mensagem) {
        System.out.println("=== ENTROU NO EMAIL SERVICE ===");
        System.out.println("Destinatário recebido: " + destinatario);
        System.out.println("Assunto recebido: " + assunto);

        String emailDestino = normalizarEmail(destinatario);
        validarDestinatario(emailDestino);

        EmailSimulado registro = new EmailSimulado();
        registro.setDestinatario(emailDestino);
        registro.setAssunto(assunto);
        registro.setMensagem(mensagem);
        registro.setStatusEnvio("REGISTRADO_NO_SISTEMA");
        EmailSimulado salvo = repository.save(registro);

        if (!envioRealHabilitado) {
            salvo.setStatusEnvio("ENVIO_REAL_DESABILITADO");
            salvo.setErroEnvio("O envio real está desabilitado. Configure app.email.envio-real=true ou APP_EMAIL_ENVIO_REAL=true.");
            System.out.println("=== ENVIO REAL DE EMAIL DESABILITADO ===");
            System.out.println(salvo.getErroEnvio());
            return repository.save(salvo);
        }

        ConfiguracaoSmtp config = montarConfiguracaoSmtp();
        salvo.setRemetente(config.remetente());

        System.out.println("=== CONFIGURAÇÃO SMTP CARREGADA ===");
        System.out.println("Host SMTP: " + config.host());
        System.out.println("Porta SMTP: " + config.porta());
        System.out.println("Usuário SMTP: " + ocultar(config.usuario()));
        System.out.println("Remetente: " + ocultar(config.remetente()));
        System.out.println("Senha configurada? " + (config.senha() != null && !config.senha().isBlank() ? "SIM" : "NÃO"));

        if (!config.estaCompleta()) {
            salvo.setStatusEnvio("PENDENTE_CONFIGURACAO_SMTP");
            salvo.setErroEnvio("Configuração SMTP incompleta. Configure MAIL_USERNAME, MAIL_PASSWORD e MAIL_FROM no application.properties ou nas variáveis de ambiente. Para Gmail, MAIL_PASSWORD deve ser senha de app, não a senha normal da conta.");
            repository.save(salvo);
            System.out.println("=== EMAIL NÃO ENVIADO: CONFIGURAÇÃO SMTP INCOMPLETA ===");
            System.out.println(salvo.getErroEnvio());
            if (exigirEnvioReal) {
                throw new IllegalStateException(salvo.getErroEnvio());
            }
            return salvo;
        }

        try {
            System.out.println("=== TENTANDO ENVIAR EMAIL SMTP ===");
            System.out.println("Para: " + emailDestino);

            JavaMailSenderImpl mailSender = criarMailSender(config);

            String mensagemTexto = normalizarMensagemTexto(mensagem);
            String mensagemHtml = montarTemplateHtml(assunto, mensagemTexto);

            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
            helper.setFrom(config.remetente(), nomeRemetente);
            helper.setReplyTo(config.remetente());
            helper.setTo(emailDestino);
            helper.setSubject(assunto);
            helper.setText(mensagemTexto, mensagemHtml);

            mailSender.send(email);

            salvo.setStatusEnvio("ENVIADO_REAL");
            salvo.setErroEnvio(null);
            EmailSimulado atualizado = repository.save(salvo);
            System.out.println("=== EMAIL SMTP ENVIADO COM SUCESSO ===");
            return atualizado;
        } catch (Exception e) {
            salvo.setStatusEnvio("ERRO_ENVIO_REAL");
            salvo.setErroEnvio(resumirErro(e));
            repository.save(salvo);
            System.out.println("=== ERRO NO ENVIO SMTP ===");
            System.out.println("Destino: " + emailDestino);
            System.out.println("Host SMTP usado: " + config.host() + ":" + config.porta());
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
            if (exigirEnvioReal) {
                throw new IllegalStateException("Falha no envio real de e-mail para " + emailDestino
                        + ". Host SMTP usado: " + config.host() + ":" + config.porta()
                        + ". Verifique MAIL_USERNAME, MAIL_PASSWORD e senha de aplicativo/token SMTP. Detalhe: "
                        + e.getMessage(), e);
            }
            return salvo;
        }
    }

    private JavaMailSenderImpl criarMailSender(ConfiguracaoSmtp config) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.host());
        sender.setPort(config.porta());
        sender.setUsername(config.usuario());
        sender.setPassword(config.senha());
        sender.setDefaultEncoding("UTF-8");

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", usaSsl(config.porta()) ? "false" : "true");
        props.put("mail.smtp.starttls.required", usaSsl(config.porta()) ? "false" : "true");
        props.put("mail.smtp.ssl.enable", usaSsl(config.porta()) ? "true" : "false");
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.debug", "false");
        return sender;
    }

    private ConfiguracaoSmtp montarConfiguracaoSmtp() {
        String usuario = normalizarEmail(usuarioSmtp);
        String senha = texto(senhaSmtp);
        String from = normalizarEmail(remetente);
        if (from.isBlank()) from = usuario;

        String host = resolverHostSmtp(hostConfigurado, usuario, from);
        int portaFinal = porta == null ? 587 : porta;
        return new ConfiguracaoSmtp(host, portaFinal, usuario, senha, from);
    }

    private String resolverHostSmtp(String hostInformado, String usuario, String from) {
        String host = texto(hostInformado);
        if (!host.isBlank() && !"auto".equalsIgnoreCase(host)) {
            // Corrige o erro comum de colocar um e-mail em MAIL_HOST.
            if (!host.contains("@")) return host;
        }

        String emailBase = !texto(usuario).isBlank() ? usuario : from;
        String dominio = dominio(emailBase);
        if (dominio.isBlank()) return "";

        return switch (dominio) {
            case "gmail.com", "googlemail.com" -> "smtp.gmail.com";
            case "hotmail.com", "outlook.com", "live.com", "msn.com" -> "smtp.office365.com";
            case "yahoo.com", "yahoo.com.br" -> "smtp.mail.yahoo.com";
            case "icloud.com", "me.com", "mac.com" -> "smtp.mail.me.com";
            case "uol.com.br" -> "smtps.uol.com.br";
            case "bol.com.br" -> "smtps.bol.com.br";
            case "terra.com.br" -> "smtp.terra.com.br";
            default -> "smtp." + dominio;
        };
    }

    private void validarDestinatario(String emailDestino) {
        if (emailDestino.isBlank() || "sem-email@local".equals(emailDestino)) {
            throw new IllegalStateException("Não foi possível notificar: destinatário sem e-mail cadastrado.");
        }
        if (!emailDestino.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalStateException("E-mail do destinatário inválido: " + emailDestino);
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private String dominio(String email) {
        String valor = normalizarEmail(email);
        int pos = valor.lastIndexOf('@');
        if (pos < 0 || pos == valor.length() - 1) return "";
        return valor.substring(pos + 1);
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private boolean usaSsl(Integer porta) {
        return porta != null && porta == 465;
    }

    private String resumirErro(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.isBlank()) return e.getClass().getSimpleName();
        return msg.length() > 950 ? msg.substring(0, 950) : msg;
    }

    private String ocultar(String valor) {
        String texto = texto(valor);
        if (texto.isBlank()) return "(vazio)";
        int arroba = texto.indexOf('@');
        if (arroba > 2) {
            return texto.substring(0, 2) + "***" + texto.substring(arroba);
        }
        if (texto.length() <= 4) return "****";
        return texto.substring(0, 2) + "***" + texto.substring(texto.length() - 2);
    }


    private String normalizarMensagemTexto(String mensagem) {
        String valor = mensagem == null ? "" : mensagem.trim();
        if (valor.isBlank()) {
            return "Notificação automática do Sistema de Moeda Estudantil.";
        }
        return valor;
    }

    private String montarTemplateHtml(String assunto, String mensagemTexto) {
        String titulo = assunto == null || assunto.isBlank()
                ? "Sistema de Moeda Estudantil"
                : assunto.trim();

        String tipo = definirTipoNotificacao(titulo, mensagemTexto);
        String corPrincipal = definirCorPrincipal(tipo);
        String corSecundaria = definirCorSecundaria(tipo);
        String corFundo = definirCorFundo(tipo);
        String emoji = definirEmoji(tipo);
        String corpoHtml = formatarCorpoHtml(mensagemTexto);
        String destaquesHtml = montarBlocosDestaque(mensagemTexto, corPrincipal, corFundo);

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s</title>
                </head>
                <body style="margin:0;padding:0;background:#eef2ff;font-family:Arial,Helvetica,sans-serif;color:#1f2937;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background:linear-gradient(135deg,#eef2ff 0%%,#f8fafc 48%%,#fff7ed 100%%);padding:28px 0;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="680" cellspacing="0" cellpadding="0" style="max-width:680px;width:94%%;background:#ffffff;border-radius:22px;overflow:hidden;border:1px solid #dbeafe;box-shadow:0 14px 36px rgba(15,23,42,0.14);">
                                    <tr>
                                        <td style="background:linear-gradient(135deg,%s 0%%,%s 100%%);padding:30px 34px;text-align:left;">
                                            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td style="vertical-align:middle;">
                                                        <div style="display:inline-block;background:rgba(255,255,255,0.18);border:1px solid rgba(255,255,255,0.34);border-radius:999px;padding:8px 14px;color:#ffffff;font-size:12px;font-weight:bold;letter-spacing:1px;text-transform:uppercase;">
                                                            %s Sistema de Moeda Estudantil
                                                        </div>
                                                        <div style="font-size:27px;line-height:1.25;color:#ffffff;font-weight:800;margin-top:16px;">
                                                            %s
                                                        </div>
                                                        <div style="font-size:14px;line-height:1.6;color:#e0f2fe;margin-top:10px;">
                                                            Notificação automática gerada pela plataforma.
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:30px 34px 14px 34px;">
                                            <div style="background:%s;border:1px solid #e0e7ff;border-radius:18px;padding:22px 24px;margin-bottom:22px;">
                                                <div style="font-size:13px;color:%s;font-weight:800;text-transform:uppercase;letter-spacing:.8px;margin-bottom:10px;">
                                                    Resumo da mensagem
                                                </div>
                                                <div style="font-size:16px;line-height:1.75;color:#334155;">
                                                    %s
                                                </div>
                                            </div>
                                            %s
                                            <div style="margin-top:24px;padding:18px 20px;background:#fff7ed;border:1px solid #fed7aa;border-left:6px solid #f97316;border-radius:14px;color:#9a3412;font-size:14px;line-height:1.6;">
                                                <strong>Atenção:</strong> esta mensagem é automática. Guarde este e-mail para consulta, especialmente em caso de recebimento de moedas, confirmação de envio ou emissão de cupom.
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:22px 34px;background:#0f172a;color:#cbd5e1;font-size:12px;line-height:1.6;text-align:center;">
                                            <strong style="color:#ffffff;">Sistema de Moeda Estudantil</strong><br>
                                            Reconhecimento de mérito, vantagens e transações acadêmicas.<br>
                                            Não responda este e-mail.
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                escaparHtml(titulo),
                corPrincipal,
                corSecundaria,
                emoji,
                escaparHtml(titulo),
                corFundo,
                corPrincipal,
                corpoHtml,
                destaquesHtml
        );
    }

    private String definirTipoNotificacao(String assunto, String mensagemTexto) {
        String base = ((assunto == null ? "" : assunto) + " " + (mensagemTexto == null ? "" : mensagemTexto)).toLowerCase(Locale.ROOT);
        if (base.contains("cupom") || base.contains("resgate") || base.contains("conferência")) return "CUPOM";
        if (base.contains("recebeu") || base.contains("você recebeu")) return "RECEBIMENTO";
        if (base.contains("confirmação") || base.contains("envio de")) return "CONFIRMACAO";
        return "GERAL";
    }

    private String definirCorPrincipal(String tipo) {
        return switch (tipo) {
            case "CUPOM" -> "#7c3aed";
            case "RECEBIMENTO" -> "#16a34a";
            case "CONFIRMACAO" -> "#2563eb";
            default -> "#0f3b63";
        };
    }

    private String definirCorSecundaria(String tipo) {
        return switch (tipo) {
            case "CUPOM" -> "#db2777";
            case "RECEBIMENTO" -> "#0f766e";
            case "CONFIRMACAO" -> "#1d4ed8";
            default -> "#1e40af";
        };
    }

    private String definirCorFundo(String tipo) {
        return switch (tipo) {
            case "CUPOM" -> "#faf5ff";
            case "RECEBIMENTO" -> "#f0fdf4";
            case "CONFIRMACAO" -> "#eff6ff";
            default -> "#f8fafc";
        };
    }

    private String definirEmoji(String tipo) {
        return switch (tipo) {
            case "CUPOM" -> "🎟️";
            case "RECEBIMENTO" -> "🪙";
            case "CONFIRMACAO" -> "✅";
            default -> "📩";
        };
    }

    private String formatarCorpoHtml(String mensagemTexto) {
        return escaparHtml(normalizarMensagemTexto(mensagemTexto))
                .replace("\r\n", "\n")
                .replace("\n", "<br>");
    }

    private String montarBlocosDestaque(String mensagemTexto, String corPrincipal, String corFundo) {
        String texto = normalizarMensagemTexto(mensagemTexto);
        StringBuilder html = new StringBuilder();
        html.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:8px 0 8px 0;\"><tr>");

        String moedas = primeiraLinhaContendo(texto, "moeda");
        String saldo = primeiraLinhaContendo(texto, "saldo");
        String motivo = primeiraLinhaContendo(texto, "motivo");
        String codigo = primeiraLinhaContendo(texto, "código");

        int total = 0;
        if (!moedas.isBlank()) {
            html.append(cardDestaque("Moedas / operação", moedas, corPrincipal, corFundo));
            total++;
        }
        if (!saldo.isBlank()) {
            html.append(cardDestaque("Saldo atualizado", saldo, "#0f766e", "#ecfdf5"));
            total++;
        }
        if (!codigo.isBlank()) {
            html.append(cardDestaque("Código do cupom", codigo, "#7c3aed", "#faf5ff"));
            total++;
        }
        if (!motivo.isBlank()) {
            html.append(cardDestaque("Motivo", valorDepoisDoisPontos(motivo), "#ea580c", "#fff7ed"));
            total++;
        }

        if (total == 0) {
            return "";
        }

        html.append("</tr></table>");
        return """
                <div style="margin:22px 0 6px 0;font-size:13px;color:#475569;font-weight:800;text-transform:uppercase;letter-spacing:.8px;">
                    Informações em destaque
                </div>
                %s
                """.formatted(html.toString());
    }

    private String cardDestaque(String rotulo, String valor, String cor, String fundo) {
        String valorSeguro = escaparHtml(valorDepoisDoisPontos(valor));
        String rotuloSeguro = escaparHtml(rotulo);
        return """
                <td style="width:50%%;padding:6px;vertical-align:top;">
                    <div style="background:%s;border:1px solid #e5e7eb;border-top:5px solid %s;border-radius:16px;padding:16px 18px;min-height:86px;">
                        <div style="font-size:12px;color:%s;font-weight:800;text-transform:uppercase;letter-spacing:.7px;margin-bottom:8px;">
                            %s
                        </div>
                        <div style="font-size:17px;color:#111827;font-weight:700;line-height:1.45;">
                            %s
                        </div>
                    </div>
                </td>
                """.formatted(fundo, cor, cor, rotuloSeguro, valorSeguro);
    }

    private String primeiraLinhaContendo(String texto, String termo) {
        if (texto == null || termo == null) return "";
        for (String linha : texto.replace("\r\n", "\n").split("\n")) {
            String normalizada = linha.trim();
            if (normalizada.toLowerCase(Locale.ROOT).contains(termo.toLowerCase(Locale.ROOT))) {
                return normalizada;
            }
        }
        return "";
    }

    private String valorDepoisDoisPontos(String valor) {
        String texto = valor == null ? "" : valor.trim();
        int idx = texto.indexOf(':');
        if (idx >= 0 && idx < texto.length() - 1) {
            return texto.substring(idx + 1).trim();
        }
        return texto;
    }

    private String escaparHtml(String valor) {
        if (valor == null) return "";
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public String getNomeRemetente() {
        return nomeRemetente;
    }

    private record ConfiguracaoSmtp(String host, Integer porta, String usuario, String senha, String remetente) {
        boolean estaCompleta() {
            return host != null && !host.isBlank()
                    && usuario != null && usuario.contains("@")
                    && senha != null && !senha.isBlank()
                    && remetente != null && remetente.contains("@");
        }
    }
}
