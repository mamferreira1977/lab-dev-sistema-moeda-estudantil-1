package app.messaging;

public class RabbitMqIndisponivelException extends RuntimeException {
    public RabbitMqIndisponivelException(Throwable cause) {
        super("RabbitMQ não está em execução ou não está acessível em localhost:5672. Inicie o RabbitMQ antes de enviar moedas. Com Docker: docker run -d --name rabbitmq-moeda -p 5672:5672 -p 15672:15672 rabbitmq:3-management", cause);
    }
}
