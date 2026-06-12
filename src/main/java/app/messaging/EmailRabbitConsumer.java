package app.messaging;

import app.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailRabbitConsumer {
    private final EmailService emailService;

    public EmailRabbitConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${app.rabbitmq.email.queue:moeda.email.queue}")
    public void consumirEmail(EmailRabbitMessage mensagem) {
        System.out.println("=== CONSUMINDO EMAIL DO RABBITMQ ===");
        System.out.println("Destinatário=" + mensagem.getDestinatario() + " | Assunto=" + mensagem.getAssunto());
        emailService.enviar(mensagem.getDestinatario(), mensagem.getAssunto(), mensagem.getMensagem());
        System.out.println("=== EMAIL PROCESSADO PELO CONSUMER ===");
    }
}
