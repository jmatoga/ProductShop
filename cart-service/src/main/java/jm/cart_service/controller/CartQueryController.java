package jm.cart_service.controller;

import jm.cart_service.application.cart.query.GetCartByCartIdQuery;
import jm.cart_service.application.cart.query.GetCartByUserIdQuery;
import jm.cart_service.application.cart.query.GetCartTotalValueQuery;
import jm.cart_service.application.cart.query.handler.GetCartByCartIdQueryHandler;
import jm.cart_service.application.cart.query.handler.GetCartByUserIdQueryHandler;
import jm.cart_service.application.cart.query.handler.GetCartTotalValueQueryHandler;
import jm.cart_service.application.mapper.CartMapper;
import jm.cart_service.domain.model.Cart;
import jm.common.dto.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/{userId}/cart")
@RequiredArgsConstructor
public class CartQueryController {
    private final GetCartByUserIdQueryHandler getCartByUserId;
    private final GetCartByCartIdQueryHandler getCartByCartId;
    private final GetCartTotalValueQueryHandler getCartTotalValue;
    private final CartMapper cartMapper;

    @GetMapping
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable UUID userId) {
        Cart cart = getCartByUserId.handle(new GetCartByUserIdQuery(userId));
        return ResponseEntity.ok(cartMapper.mapToDTO(cart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable UUID userId, @PathVariable UUID cartId) {
        Cart cart = getCartByCartId.handle(new GetCartByCartIdQuery(userId, cartId));
        return ResponseEntity.ok(cartMapper.mapToDTO(cart));
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal(@PathVariable UUID userId) {
        double totalValue = getCartTotalValue.handle(new GetCartTotalValueQuery(userId));
        return ResponseEntity.ok(totalValue);
    }
}
