package jm.product_service.application.reservation.command;

import java.util.UUID;

public record ReserveProductCommand(UUID productId, UUID cartId, int quantity) {}