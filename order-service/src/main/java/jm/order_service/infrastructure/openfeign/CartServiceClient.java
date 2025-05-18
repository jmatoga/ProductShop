package jm.order_service.infrastructure.openfeign;

import jm.common.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@FeignClient(name = "cart-service", url = "${external.cart-service.base-url}")
@RequestMapping("/api/v1/user/{userId}")
public interface CartServiceClient {
    @GetMapping("/cart/{cartId}")
    CartDTO getCheckedOutCart(@PathVariable("userId") UUID userId, @PathVariable("cartId") UUID cartId);
}
