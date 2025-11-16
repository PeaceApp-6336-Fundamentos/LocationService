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

    // -------------------------------
    // REPORT EXCHANGE (EVENTOS DEL REPORTE)
    // -------------------------------

    @Value("${app.broker.exchange.report}")
    private String reportExchangeName;

    // ---- EVENTO: APPROVED ----
    @Value("${app.broker.queue.report.approved}")
    private String reportApprovedQueue;

    @Value("${app.broker.routing-key.report.approved}")
    private String reportApprovedRK;

    // ---- EVENTO: DELETED ----
    @Value("${app.broker.queue.report.deleted}")
    private String reportDeletedQueue;

    @Value("${app.broker.routing-key.report.deleted}")
    private String reportDeletedRK;

    // ---- EVENTO: REJECTED ----
    @Value("${app.broker.queue.report.rejected}")
    private String reportRejectedQueue;

    @Value("${app.broker.routing-key.report.rejected}")
    private String reportRejectedRK;


    // -------------------------------
    // EXCHANGE DEL REPORTE
    // -------------------------------
    @Bean
    public TopicExchange reportExchange() {
        return new TopicExchange(reportExchangeName);
    }


    // -------------------------------
    // QUEUE + BINDINGS : APPROVED
    // -------------------------------
    @Bean
    public Queue reportApprovedQueue() {
        return new Queue(reportApprovedQueue, true);
    }

    @Bean
    public Binding reportApprovedBinding() {
        return BindingBuilder.bind(reportApprovedQueue())
                .to(reportExchange())
                .with(reportApprovedRK);
    }


    // -------------------------------
    // QUEUE + BINDINGS : DELETED
    // -------------------------------
    @Bean
    public Queue reportDeletedQueue() {
        return new Queue(reportDeletedQueue, true);
    }

    @Bean
    public Binding reportDeletedBinding() {
        return BindingBuilder.bind(reportDeletedQueue())
                .to(reportExchange())
                .with(reportDeletedRK);
    }


    // -------------------------------
    // QUEUE + BINDINGS : REJECTED
    // -------------------------------
    @Bean
    public Queue reportRejectedQueue() {
        return new Queue(reportRejectedQueue, true);
    }

    @Bean
    public Binding reportRejectedBinding() {
        return BindingBuilder.bind(reportRejectedQueue())
                .to(reportExchange())
                .with(reportRejectedRK);
    }


    // -------------------------------
    // JSON CONVERTER
    // -------------------------------
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    // -------------------------------
    // TEMPLATE
    // -------------------------------
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // -------------------------------
    // LISTENER FACTORY
    // -------------------------------
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
