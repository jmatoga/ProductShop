package jm.cart_service.domain.exception;

import java.util.UUID;

public class ProductLockedException extends RuntimeException {
    public ProductLockedException(UUID productId) {
        super("Product with id: " + productId + " is locked");
    }
}
