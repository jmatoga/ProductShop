package jm.cart_service.controller;

import jm.cart_service.application.cart.command.AddProductToCartCommand;
import jm.cart_service.application.cart.command.CheckoutCartCommand;
import jm.cart_service.application.cart.command.CreateCartCommand;
import jm.cart_service.application.cart.command.RemoveProductFromCartCommand;
import jm.cart_service.application.cart.command.handler.AddProductToCartCommandHandler;
import jm.cart_service.application.cart.command.handler.CheckoutCartCommandHandler;
import jm.cart_service.application.cart.command.handler.CreateCartCommandHandler;
import jm.cart_service.application.cart.command.handler.RemoveProductFromCartCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/{userId}/cart")
@RequiredArgsConstructor
public class CartCommandController {
    private final AddProductToCartCommandHandler addHandler;
    private final RemoveProductFromCartCommandHandler removeHandler;
    private final CreateCartCommandHandler createHandler;
    private final CheckoutCartCommandHandler checkoutHandler;

    @PostMapping()
    public ResponseEntity<String> createCart(@PathVariable UUID userId) {
        createHandler.handle(CreateCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok("Cart created successfully");
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@PathVariable UUID userId, @RequestParam UUID productId, @RequestParam int quantity) {
        addHandler.handle(AddProductToCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok("Product added successfully");
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<String> removeProduct(@PathVariable UUID userId, @RequestParam UUID productId, @RequestParam int quantity) {
        removeHandler.handle(RemoveProductFromCartCommand.builder().userId(userId).productId(productId).quantity(quantity).build());
        return ResponseEntity.ok("Product removed successfully");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@PathVariable UUID userId) {
        checkoutHandler.handle(CheckoutCartCommand.builder().userId(userId).build());
        return ResponseEntity.ok("Checkedout successfully");
    }
}
