package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.CheckoutCartCommand;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.messaging.EventPublisher;
import jm.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import jm.cart_service.infrastructure.redis.CartActivityService;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckoutCartCommandHandler {
    private final CartRepository cartRepository;
    private final EventPublisher eventPublisher;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;

    @Transactional
    public void handle(CheckoutCartCommand cmd) {
        Cart cart = cartRepository.findByUserId(cmd.userId())
                            .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        cart.checkout();
        cartRepository.save(cart);
        productClient.releaseByCart(cart.getId());
        activityService.expireCartTtl(cart.getId());
        cart.pullDomainEvents().forEach(eventPublisher::publish);
    }
}
