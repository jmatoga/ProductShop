package jm.cart_service.application.cart.query;

import java.util.UUID;

import static jm.common.util.UtilHelper.*;


public record GetCartByCartIdQuery(UUID userId, UUID cartId) {
    public GetCartByCartIdQuery {
        checkNullOrEmpty(userId, USER_NAME);
        checkNullOrEmpty(cartId, CART_NAME);
    }
}
