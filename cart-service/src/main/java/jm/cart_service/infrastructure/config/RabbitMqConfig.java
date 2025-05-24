package jm.cart_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String CART_EVENTS_EXCHANGE_KEY = "cart.events.exchange";
    public static final String ORDER_EVENTS_EXCHANGE_KEY = "order.events.exchange";

    @Bean
    public TopicExchange cartEventsExchange() {
        return new TopicExchange(CART_EVENTS_EXCHANGE_KEY, true, false);
    }

    @Bean
    public DirectExchange orderEventsExchange() {
        return new DirectExchange(ORDER_EVENTS_EXCHANGE_KEY, true, false);
    }

    @Bean
    public Queue cartCreatedQueue() {
        return new Queue("cart.created.queue", true);
    }

    @Bean
    public Queue productReservedQueue() {
        return new Queue("product.reserved.queue", true);
    }

    @Bean
    public Queue productRemovedQueue() {
        return new Queue("product.removed.queue", true);
    }

    @Bean
    public Queue cartExpiredQueue() {
        return new Queue("cart.expired.queue", true);
    }

    @Bean
    public Queue cartCheckedoutQueue() {
        return new Queue("cart.checkedout.queue", true);
    }

    @Bean
    public Binding cartCreatedBinding() {
        return BindingBuilder.bind(cartCreatedQueue())
                       .to(cartEventsExchange())
                       .with("cart.created");
    }

    @Bean
    public Binding productReservedBinding() {
        return BindingBuilder.bind(productReservedQueue())
                       .to(cartEventsExchange())
                       .with("product.reserved");
    }

    @Bean
    public Binding productRemovedBinding() {
        return BindingBuilder.bind(productRemovedQueue())
                       .to(cartEventsExchange())
                       .with("product.removed");
    }

    @Bean
    public Binding cartExpiredBinding() {
        return BindingBuilder.bind(cartExpiredQueue())
                       .to(cartEventsExchange())
                       .with("cart.expired");
    }

    @Bean
    public Binding cartCheckedOutBinding() {
        return BindingBuilder.bind(cartCheckedoutQueue())
                       .to(orderEventsExchange())
                       .with("cart.checkedout");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri("amqp://guest:guest@rabbitmq:5672");
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarables declarables() {
        return new Declarables(cartEventsExchange(), orderEventsExchange(), cartCreatedQueue(), productReservedQueue(), productRemovedQueue(), cartExpiredQueue(), cartCheckedoutQueue());
    }
}
