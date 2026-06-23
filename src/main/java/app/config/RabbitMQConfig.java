package app.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${app.rabbitmq.email.exchange:moeda.email.exchange}")
    private String emailExchange;

    @Value("${app.rabbitmq.email.queue:moeda.email.queue}")
    private String emailQueue;

    @Value("${app.rabbitmq.email.routing-key:moeda.email.enviar}")
    private String emailRoutingKey;

    @Bean
    public DirectExchange emailDirectExchange() {
        return new DirectExchange(emailExchange, true, false);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueue, true);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailDirectExchange) {
        return BindingBuilder.bind(emailQueue).to(emailDirectExchange).with(emailRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
