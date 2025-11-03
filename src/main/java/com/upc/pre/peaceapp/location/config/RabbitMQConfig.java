package com.upc.pre.peaceapp.location.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${app.broker.exchange.report}")
    private String exchangeName;

    @Value("${app.broker.queue.report.deleted}")
    private String queueName;

    @Value("${app.broker.routing-key.report.deleted}")
    private String routingKey;
    @Bean
    public TopicExchange locationExchange() {
        return new TopicExchange("location.exchange");
    }

    @Bean
    public Queue locationDeletedQueue() {
        return new Queue("location.deleted.queue", true);
    }

    @Bean
    public Binding locationDeletedBinding() {
        return BindingBuilder.bind(locationDeletedQueue())
                .to(locationExchange())
                .with("location.deleted");
    }

    @Bean
    public TopicExchange reportExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue reportDeletedQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding reportDeletedBinding() {
        return BindingBuilder.bind(reportDeletedQueue())
                .to(reportExchange())
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
