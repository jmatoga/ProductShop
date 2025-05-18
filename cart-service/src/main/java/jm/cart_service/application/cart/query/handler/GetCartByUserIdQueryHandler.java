package jm.cart_service.application.cart.query.handler;

import jm.cart_service.application.cart.query.GetCartByUserIdQuery;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCartByUserIdQueryHandler {
    private final CartRepository cartRepository;

    public Cart handle(GetCartByUserIdQuery query) {
        return cartRepository.findByUserId(query.userId())
                       .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}