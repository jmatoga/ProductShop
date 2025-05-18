package jm.product_service.controller;

import jm.common.dto.ProductDTO;
import jm.product_service.application.mapper.ProductMapper;
import jm.product_service.application.product.query.GetProductQuery;
import jm.product_service.application.product.query.GetProductQueryHandler;
import jm.product_service.application.reservation.command.ReleaseProductByCartCommand;
import jm.product_service.application.reservation.command.ReleaseProductCommand;
import jm.product_service.application.reservation.command.ReserveProductCommand;
import jm.product_service.application.reservation.command.handler.ReleaseProductByCartCommandHandler;
import jm.product_service.application.reservation.command.handler.ReleaseProductCommandHandler;
import jm.product_service.application.reservation.command.handler.ReserveProductCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {
    private final ReserveProductCommandHandler reserveHandler;
    private final ReleaseProductCommandHandler releaseProductHandler;
    private final ReleaseProductByCartCommandHandler releaseByCartHandler;
    private final GetProductQueryHandler queryHandler;
    private final ProductMapper productMapper;

    @PostMapping("/{productId}/reserve")
    public ResponseEntity<ProductDTO> reserveProduct(@PathVariable UUID productId, @RequestParam UUID cartId,
                                                     @RequestParam int quantity) {
        reserveHandler.handle(new ReserveProductCommand(productId, cartId, quantity));
        ProductDTO product = mapToProductDTO(productId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}/release")
    public ResponseEntity<ProductDTO> releaseProductPartially(@PathVariable UUID productId, @RequestParam UUID cartId,
                                                              @RequestParam int quantity) {
        releaseProductHandler.handle(new ReleaseProductCommand(productId, cartId, quantity));
        ProductDTO product = mapToProductDTO(productId);
        return ResponseEntity.ok(product);
    }

    private ProductDTO mapToProductDTO(UUID productId) {
        return productMapper.mapToDTO(queryHandler.handle(new GetProductQuery(productId)));
    }

    @DeleteMapping("/release-by-cart/{cartId}")
    public ResponseEntity<String> releaseByCart(@PathVariable UUID cartId) {
        releaseByCartHandler.handle(new ReleaseProductByCartCommand(cartId));
        return ResponseEntity.ok("Product released successfully");
    }
}
