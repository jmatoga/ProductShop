package jm.order_service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String ORDER_EVENTS_EXCHANGE = "order-events-exchange";

    @Bean
    public Queue cartCheckedOutQueue() {
        return new Queue("cart.checkedout.queue", true);
    }

    @Bean
    public DirectExchange orderEventsExchange() {
        return new DirectExchange(ORDER_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Binding cartCheckedOutBinding(Queue cartCheckedOutQueue, DirectExchange orderEventsExchange) {
        return BindingBuilder.bind(cartCheckedOutQueue)
                       .to(orderEventsExchange)
                       .with("cart.checkedout");
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
