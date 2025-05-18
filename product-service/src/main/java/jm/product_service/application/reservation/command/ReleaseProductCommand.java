package jm.product_service.application.reservation.command;

import java.util.UUID;

public record ReleaseProductCommand(UUID productId, UUID cartId, int quantity) {}