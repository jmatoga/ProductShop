package jm.order_service.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jm.common.event.CartCheckedOutEvent;
import jm.order_service.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartEventListener {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @RabbitListener(queues = "cart.checkedout.queue")
    public void handleCartCheckedOut(String message) {
        try {
            CartCheckedOutEvent event = objectMapper.readValue(message, CartCheckedOutEvent.class);
            orderService.createOrder(event.userId(), event.cartId());
        } catch (Exception e) {
            log.error("Error while processing CartCheckedOutEvent: {}", e.getMessage());
        }
    }
}