package app.service;

import app.model.EmailSimulado;
import org.springframework.stereotype.Service;

@Service
public class EmailSimuladoService {
    private final EmailService emailService;

    public EmailSimuladoService(EmailService emailService) {
        this.emailService = emailService;
    }

    public EmailSimulado enviar(String destinatario, String assunto, String mensagem) {
        return emailService.enviar(destinatario, assunto, mensagem);
    }
}
