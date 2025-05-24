package jm.cart_service.infrastructure.messaging;

import jm.common.event.CartCheckedOutEvent;
import jm.cart_service.domain.event.CartCreatedEvent;
import jm.cart_service.domain.event.CartExpiredEvent;
import jm.common.event.ProductRemovedEvent;
import jm.common.event.ProductReservedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventPublisher implements jm.cart_service.infrastructure.messaging.EventPublisher {
    public static final String PRODUCT_RESERVED_ROUTING_KEY = "product.reserved";
    public static final String PRODUCT_REMOVED_ROUTING_KEY = "product.removed";
    public static final String CART_CHECKEDOUT_ROUTING_KEY = "cart.checkedout";
    public static final String CART_CREATED_ROUTING_KEY = "cart.created";
    public static final String PRODUCT_EXPIRED_ROUTING_KEY = "cart.expired";

    private final RabbitTemplate rabbit;
    private final String cartEventsExchange;
    private final String orderEventsExchange;

    public RabbitMqEventPublisher(RabbitTemplate rabbit,
                                  @Value("${messaging.exchange.cart-events}") String cartEventsExchange,
                                  @Value("${messaging.exchange.order-events}") String orderEventsExchange) {
        this.rabbit = rabbit;
        this.cartEventsExchange = cartEventsExchange;
        this.orderEventsExchange = orderEventsExchange;
    }

    @Override
    public void publish(Object domainEvent) {
        try {
            switch (domainEvent) {
                case ProductReservedEvent ignored -> rabbit.convertAndSend(cartEventsExchange, PRODUCT_RESERVED_ROUTING_KEY, domainEvent);
                case ProductRemovedEvent ignored -> rabbit.convertAndSend(cartEventsExchange, PRODUCT_REMOVED_ROUTING_KEY, domainEvent);
                case CartCheckedOutEvent ignored -> rabbit.convertAndSend(orderEventsExchange, CART_CHECKEDOUT_ROUTING_KEY, domainEvent);
                case CartCreatedEvent ignored -> rabbit.convertAndSend(cartEventsExchange, CART_CREATED_ROUTING_KEY, domainEvent);
                case CartExpiredEvent ignored -> rabbit.convertAndSend(cartEventsExchange, PRODUCT_EXPIRED_ROUTING_KEY, domainEvent);
                case null -> throw new IllegalArgumentException("Domain event cannot be null");
                default -> throw new IllegalArgumentException("Unsupported event: " + domainEvent.getClass().getName());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Event serialization error: " + domainEvent, ex);
        }
    }
}