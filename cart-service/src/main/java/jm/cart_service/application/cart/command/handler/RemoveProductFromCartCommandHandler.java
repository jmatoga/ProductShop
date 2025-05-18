package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.RemoveProductFromCartCommand;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.messaging.EventPublisher;
import jm.cart_service.infrastructure.openfeign.ExternalProductServiceClient;
import jm.cart_service.infrastructure.redis.CartActivityService;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveProductFromCartCommandHandler {
    private final CartRepository cartRepository;
    private final EventPublisher eventPublisher;
    private final ExternalProductServiceClient productClient;
    private final CartActivityService activityService;

    @Transactional
    public void handle(RemoveProductFromCartCommand cmd) {
        Cart cart = cartRepository.findByUserId(cmd.userId())
                            .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + cmd.userId()));

        cart.removeProduct(cmd.productId(), cmd.quantity());
        cartRepository.save(cart);
        productClient.releaseProductPartially(cmd.productId(), cmd.userId(), cart.getId(), cmd.quantity());
        activityService.expireCartTtl(cart.getId()); // Reset ważności koszyka
        cart.pullDomainEvents().forEach(eventPublisher::publish); // Publikacja zdarzeń (ProductRemovedEvent)
    }
}