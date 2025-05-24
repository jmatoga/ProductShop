package jm.order_service.infrastructure.messaging;

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
    private final OrderService orderService;

    @RabbitListener(queues = "cart.checkedout.queue")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        try {
            orderService.createOrder(event.userId(), event.cartId());
        } catch (Exception e) {
            log.error("Error while processing CartCheckedOutEvent: {}", e.getMessage());
        }
    }
}