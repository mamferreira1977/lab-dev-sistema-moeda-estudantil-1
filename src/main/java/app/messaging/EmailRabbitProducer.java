package app.messaging;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailRabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.email.exchange:moeda.email.exchange}")
    private String emailExchange;

    @Value("${app.rabbitmq.email.routing-key:moeda.email.enviar}")
    private String emailRoutingKey;

    public EmailRabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarEmail(String destinatario, String assunto, String mensagem) {
        try {
            System.out.println("=== ENTROU NO PRODUCER RABBITMQ ===");
            System.out.println("Exchange=" + emailExchange + " | RoutingKey=" + emailRoutingKey + " | Destinatário=" + destinatario);
            EmailRabbitMessage payload = new EmailRabbitMessage(destinatario, assunto, mensagem);
            rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, payload);
            System.out.println("=== PUBLICOU NO RABBITMQ ===");
        } catch (AmqpConnectException e) {
            throw new RabbitMqIndisponivelException(e);
        } catch (AmqpException e) {
            throw new IllegalStateException("Falha ao publicar mensagem de e-mail na fila RabbitMQ. Verifique se o RabbitMQ está ativo e se as configurações spring.rabbitmq.* estão corretas.", e);
        }
    }
}
