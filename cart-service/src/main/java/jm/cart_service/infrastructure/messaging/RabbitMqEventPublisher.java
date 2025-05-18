package jm.cart_service.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jm.common.event.CartCheckedOutEvent;
import jm.cart_service.domain.event.CartCreatedEvent;
import jm.cart_service.domain.event.CartExpiredEvent;
import jm.cart_service.domain.event.ProductRemovedEvent;
import jm.cart_service.domain.event.ProductReservedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventPublisher implements jm.cart_service.infrastructure.messaging.EventPublisher {
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper;
    private final String cartEventsExchange;
    private final String orderEventsExchange;

    public RabbitMqEventPublisher(RabbitTemplate rabbit, ObjectMapper mapper,
                                  @Value("${messaging.exchange.cart-events}") String cartEventsExchange,
                                  @Value("${messaging.exchange.order-events}") String orderEventsExchange) {
        this.rabbit = rabbit;
        this.mapper = mapper;
        this.cartEventsExchange = cartEventsExchange;
        this.orderEventsExchange = orderEventsExchange;
    }

    @Override
    public void publish(Object domainEvent) {
        try {
            String payload = mapper.writeValueAsString(domainEvent);

            switch (domainEvent) {
                case ProductReservedEvent ignored ->
                        rabbit.convertAndSend(cartEventsExchange, "product.reserved", payload);
                case ProductRemovedEvent ignored ->
                        rabbit.convertAndSend(cartEventsExchange, "product.removed", payload);
                case CartCheckedOutEvent ignored ->
                        rabbit.convertAndSend(orderEventsExchange, "cart.checkedout", payload);
                case CartCreatedEvent ignored ->
                        rabbit.convertAndSend(cartEventsExchange, "cart.created", payload);
                case CartExpiredEvent ignored ->
                        rabbit.convertAndSend(cartEventsExchange, "cart.expired", payload);
                case null, default -> {
                    assert domainEvent != null;
                    throw new IllegalArgumentException("Unsupported event: " + domainEvent.getClass());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Event serialization error: " + domainEvent, ex);
        }
    }
}