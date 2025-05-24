package jm.cart_service.application.cart.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddProductToCartCommand(UUID userId, UUID productId, int quantity, double price) {}