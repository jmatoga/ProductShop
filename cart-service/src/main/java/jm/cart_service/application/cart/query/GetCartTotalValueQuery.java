package jm.cart_service.application.cart.query;

import java.util.UUID;

import static jm.common.util.UtilHelper.*;

public record GetCartTotalValueQuery(UUID userId) {
    public GetCartTotalValueQuery {
        checkNullOrEmpty(userId, USER_NAME);
    }
}
