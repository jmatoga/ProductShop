package jm.cart_service.application.cart.command.handler;

import jakarta.transaction.Transactional;
import jm.cart_service.application.cart.command.CreateCartCommand;
import jm.cart_service.domain.event.CartCreatedEvent;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.messaging.EventPublisher;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateCartCommandHandler {
    private final CartRepository cartRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Cart handle(CreateCartCommand cmd) {
        Cart cart;
        Optional<Cart> optionalCart = cartRepository.findByUserId(cmd.userId());

        if(optionalCart.isEmpty()) {
            cart = Cart.create(cmd.userId());
            cartRepository.save(cart);
            cart.getDomainEvents().add(new CartCreatedEvent(cart.getId(), cart.getUserId()));
        } else {
            cart = optionalCart.get();
        }

        cart.pullDomainEvents().forEach(eventPublisher::publish);
        return cart;
    }
}
