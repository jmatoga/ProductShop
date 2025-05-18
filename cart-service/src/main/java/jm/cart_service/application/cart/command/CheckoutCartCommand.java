package jm.cart_service.application.cart.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CheckoutCartCommand(UUID userId) {}