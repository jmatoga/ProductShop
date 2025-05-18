package jm.cart_service.application.cart.query;

import java.util.UUID;

import static jm.common.util.UtilHelper.*;


public record GetCartByUserIdQuery(UUID userId) {
    public GetCartByUserIdQuery {
        checkNullOrEmpty(userId, USER_NAME);
    }
}
