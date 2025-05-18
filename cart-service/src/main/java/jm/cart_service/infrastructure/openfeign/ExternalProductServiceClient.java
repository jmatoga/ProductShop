package jm.cart_service.infrastructure.openfeign;

import jm.common.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@FeignClient(name = "product-service", url = "${external.product-service.base-url}")
public interface ExternalProductServiceClient {
    @GetMapping("/api/v1/products/{productId}")
    ProductDTO getProductById(@PathVariable UUID productId);

    @PostMapping("/api/v1/products/{productId}/reserve")
    ProductDTO reserveProduct(@PathVariable UUID productId, @RequestParam UUID userId, @RequestParam UUID cartId, @RequestParam int quantity);

    @DeleteMapping("api/v1/products/{productId}/release")
    ProductDTO releaseProductPartially(@PathVariable UUID productId, @RequestParam UUID userId, @RequestParam UUID cartId, @RequestParam int quantity);

    @DeleteMapping("api/v1/products/release-by-cart/{cartId}")
    ProductDTO releaseByCart(@PathVariable UUID cartId);
}
