package jm.cart_service.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartActivityService {
    private static final Duration CART_TTL = Duration.ofSeconds(20);
    private final RedisTemplate<String, Object> redis;

    // Odświeża Time To Live przy każdej operacji
    public void refreshCartTtl(UUID cartId) {
        String key = "cart:active:" + cartId;
        redis.opsForValue().set(key, System.currentTimeMillis(), CART_TTL);
        log.info("Refreshed TTL of {}", key);
    }

    public void expireCartTtl(UUID cartId) {
        String key = "cart:active:" + cartId;
        redis.expire(key, CART_TTL);
        log.info("Expired TTL of {}", key);
    }
}
