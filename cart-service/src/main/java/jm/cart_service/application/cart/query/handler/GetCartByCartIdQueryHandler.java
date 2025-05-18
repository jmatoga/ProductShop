package jm.cart_service.application.cart.query.handler;

import jm.cart_service.application.cart.query.GetCartByCartIdQuery;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCartByCartIdQueryHandler {
    private final CartRepository cartRepository;

    public Cart handle(GetCartByCartIdQuery query) {
        Cart cart = cartRepository.findById(query.cartId())
                            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getUserId().equals(query.userId())) {
            throw new IllegalArgumentException("User ID does not match cart owner");
        }
        return cart;
    }
}
