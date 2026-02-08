package com.viago.service.config;

// Disabled RabbitMQ configuration for now
// Uncomment when RabbitMQ is needed

/*
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "viago.notification.exchange";
    public static final String QUEUE = "viago.email.queue";
    public static final String ROUTING_KEY = "viago.email.routingKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
*/

/**
 * RabbitMQ Configuration - Currently Disabled
 * 
 * This configuration is commented out to allow the service to run without RabbitMQ.
 * To enable RabbitMQ:
 * 1. Uncomment the code above
 * 2. Remove the RabbitAutoConfiguration exclusion from application.yml
 * 3. Ensure RabbitMQ server is running
 */
public class RabbitMQConfig {
    public static final String EXCHANGE = "viago.notification.exchange";
    public static final String QUEUE = "viago.email.queue";
    public static final String ROUTING_KEY = "viago.email.routingKey";
}
