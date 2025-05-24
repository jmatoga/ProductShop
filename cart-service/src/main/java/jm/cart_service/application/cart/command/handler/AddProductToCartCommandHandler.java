package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.AddProductToCartCommand;
import jm.cart_service.application.cart.command.CreateCartCommand;
import jm.cart_service.domain.exception.ProductNotAvailableException;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import jm.cart_service.infrastructure.redis.CartActivityService;
import jm.cart_service.infrastructure.repository.CartRepository;
import jm.common.dto.ProductDTO;
import jm.common.event.ProductReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static jm.cart_service.domain.model.ECartItemStatus.RESERVED;
import static jm.cart_service.infrastructure.config.RabbitMqConfig.CART_EVENTS_EXCHANGE_KEY;
import static jm.cart_service.infrastructure.messaging.RabbitMqEventPublisher.PRODUCT_RESERVED_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepository;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;
    private final CreateCartCommandHandler createCartCommandHandler;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        Cart cart = cartRepository.findByUserId(cmd.userId())
                            .orElseGet(() -> createCartCommandHandler.handle(new CreateCartCommand(cmd.userId())));

        ProductDTO product = productClient.getProductById(cmd.productId());

        if (product == null) {
            throw new IllegalArgumentException("Product with ID: " + cmd.productId() + " not found.");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product with ID: " + cmd.productId() + " has invalid price: " + product.getPrice());
        }
        if (product.getAvailableQuantity() < cmd.quantity()) {
            throw new ProductNotAvailableException(cmd.productId(), cmd.quantity(), product.getAvailableQuantity());
        }

        cart.addProduct(cmd.productId(), cmd.quantity(), product.getPrice());

        cart.pullDomainEvents().forEach(event -> {
            if (event instanceof ProductReservedEvent) {
                UUID cartItemId = (UUID) rabbitTemplate.convertSendAndReceive(CART_EVENTS_EXCHANGE_KEY, PRODUCT_RESERVED_ROUTING_KEY, event);
                activityService.refreshCartTtl(cart.getId());
                cart.getItems().stream()
                        .filter(cartItem -> cartItem.getId().equals(cartItemId))
                        .findFirst()
                        .ifPresent(cartItem -> cartItem.setStatus(RESERVED));
                cartRepository.save(cart);
            }
        });
    }
}
