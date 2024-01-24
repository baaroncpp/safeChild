package com.nc.safechild.base.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Configuration
public class RabbitMQConfig {

    @Value("${notification.exchange.name}")
    private String exchange;

    @Value("${notification.queue.name.sms}")
    private String smsQueue;

    @Value("${notification.routing.key.sms}")
    private String smsRoutingKey;

    @Bean
    Queue smsQueue(){
        return new Queue(smsQueue);
    }

    @Bean
    Binding getSmsBinding(@Qualifier("smsQueue") Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(smsRoutingKey);
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
