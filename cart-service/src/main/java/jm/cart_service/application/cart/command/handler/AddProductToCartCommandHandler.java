package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.AddProductToCartCommand;
import jm.cart_service.application.cart.command.CreateCartCommand;
import jm.cart_service.domain.event.ProductReservedEvent;
import jm.cart_service.domain.exception.ProductNotAvailableException;
import jm.cart_service.domain.model.Cart;
import jm.common.dto.ProductDTO;
import jm.cart_service.infrastructure.messaging.EventPublisher;
import jm.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import jm.cart_service.infrastructure.redis.CartActivityService;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddProductToCartCommandHandler {
    private final CartRepository cartRepository;
    private final ExternalProductServiceClient productClient;
    private final EventPublisher eventPublisher;
    private final CartActivityService activityService;
    private final jm.cart_service.application.cart.command.handler.CreateCartCommandHandler createCartCommandHandler;

    @Transactional
    public void handle(AddProductToCartCommand cmd) {
        Cart cart = cartRepository.findByUserId(cmd.userId())
                            .orElseGet(() -> createCartCommandHandler.handle(new CreateCartCommand(cmd.userId())));

        ProductDTO product = productClient.getProductById(cmd.productId());

        if (product.getAvailableQuantity() < cmd.quantity()) {
            throw new ProductNotAvailableException(cmd.productId(), cmd.quantity(), product.getAvailableQuantity());
        }

        cart.addProduct(cmd.productId(), cmd.quantity(), product.getPrice());
        cartRepository.save(cart);
        productClient.reserveProduct(cmd.productId(), cmd.userId(), cart.getId(), cmd.quantity());
        activityService.refreshCartTtl(cart.getId());
        cart.pullDomainEvents().forEach(event -> {
            if (event instanceof ProductReservedEvent) {
                eventPublisher.publish(event);
            }
        });
    }
}
