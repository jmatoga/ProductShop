package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.RemoveProductFromCartCommand;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.domain.model.CartItem;
import jm.cart_service.infrastructure.redis.CartActivityService;
import jm.cart_service.infrastructure.repository.CartRepository;
import jm.common.event.ProductRemovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

import static jm.cart_service.domain.model.ECartItemStatus.CANCELLED;
import static jm.cart_service.infrastructure.config.RabbitMqConfig.CART_EVENTS_EXCHANGE_KEY;
import static jm.cart_service.infrastructure.messaging.RabbitMqEventPublisher.PRODUCT_REMOVED_ROUTING_KEY;

@Component
@RequiredArgsConstructor
public class RemoveProductFromCartCommandHandler {
    private final CartRepository cartRepository;
    private final CartActivityService activityService;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void handle(RemoveProductFromCartCommand cmd) {
        Cart cart = cartRepository.findByUserId(cmd.userId())
                            .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        cart.removeProduct(cmd.productId(), cmd.quantity());

        cart.pullDomainEvents().forEach(event -> {
            if (event instanceof ProductRemovedEvent) {
                UUID cartItemId = (UUID) rabbitTemplate.convertSendAndReceive(CART_EVENTS_EXCHANGE_KEY, PRODUCT_REMOVED_ROUTING_KEY, event);
                cart.getItems().stream()
                        .filter(cartItem -> cartItem.getId().equals(cartItemId))
                        .findFirst()
                        .ifPresent(decreaseQuantity(cmd));
                cartRepository.save(cart);
                activityService.expireCartTtl(cart.getId()); // Reset ważności koszyka
            }
        });
    }

    private Consumer<CartItem> decreaseQuantity(RemoveProductFromCartCommand cmd) {
        return cartItem -> {
            if (cartItem.getQuantity() > cmd.quantity()) {
                cartItem.decreaseQuantity(cmd.quantity());
            } else {
                cartItem.setStatus(CANCELLED);
            }
        };
    }
}