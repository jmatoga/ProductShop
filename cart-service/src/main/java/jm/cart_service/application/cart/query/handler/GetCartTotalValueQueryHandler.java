package jm.cart_service.application.cart.query.handler;

import jm.cart_service.application.cart.query.GetCartTotalValueQuery;
import jm.cart_service.domain.model.Cart;
import jm.cart_service.infrastructure.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCartTotalValueQueryHandler {
    private final CartRepository cartRepository;

    public double handle(GetCartTotalValueQuery query) {
        Cart cart = cartRepository.findByUserId(query.userId())
                            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        return cart.getItems().stream()
                       .mapToDouble(item -> item.getPrice() * item.getQuantity())
                       .sum();
    }
}