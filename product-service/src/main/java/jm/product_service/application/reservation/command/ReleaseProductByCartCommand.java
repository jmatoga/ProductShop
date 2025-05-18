package jm.product_service.application.reservation.command;

import java.util.UUID;

public record ReleaseProductByCartCommand(UUID cartId) {}