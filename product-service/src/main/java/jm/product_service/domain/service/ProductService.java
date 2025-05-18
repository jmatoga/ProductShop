package jm.product_service.domain.service;

import java.util.UUID;

public interface ProductService {
    void decreaseStock(UUID productId, int incomingQuantity);

    void increaseStock(UUID productId, int incomingQuantity);
}
