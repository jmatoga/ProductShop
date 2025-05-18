package jm.cart_service.application.cart.command;

import lombok.Builder;

import java.util.UUID;


@Builder
public record RemoveProductFromCartCommand(UUID userId, UUID productId, int quantity) {}