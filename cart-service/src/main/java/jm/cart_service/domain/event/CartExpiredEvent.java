package jm.cart_service.domain.event;

import java.util.UUID;

public record CartExpiredEvent(UUID cartId, UUID userId) {}
