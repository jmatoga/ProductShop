package jm.cart_service.domain.exception;

import java.util.UUID;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException(UUID productId, int requestedQuantity, int availableQuantity) {
        super(String.format("Product %s not available in requested quantity. Requested: %d, Available: %d", productId,
                requestedQuantity, availableQuantity));
    }
}
