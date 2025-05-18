package jm.order_service.domain.service;

import java.util.UUID;

public interface OrderService {
    void createOrder(UUID userId, UUID cartId);
}
