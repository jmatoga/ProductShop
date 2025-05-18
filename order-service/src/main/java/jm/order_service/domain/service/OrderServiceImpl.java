package jm.order_service.domain.service;

import jakarta.transaction.Transactional;
import jm.common.dto.CartDTO;
import jm.order_service.domain.model.EOrderStatus;
import jm.order_service.domain.model.Order;
import jm.order_service.domain.model.OrderItem;
import jm.order_service.infrastructure.openfeign.CartServiceClient;
import jm.order_service.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements jm.order_service.domain.service.OrderService {
    private final OrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;

    @Override
    @Transactional
    public void createOrder(UUID userId, UUID cartId) {
        CartDTO cartDTO = cartServiceClient.getCheckedOutCart(userId, cartId);

        double totalAmount = cartDTO.getItems().stream()
                                     .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                     .sum();


        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                              .id(orderId)
                              .userId(userId)
                              .cartId(cartId)
                              .status(EOrderStatus.PLACED)
                              .totalAmount(totalAmount)
                              .createdAt(Instant.now())
                              .updatedAt(Instant.now())
                              .build();

        List<OrderItem> items = cartDTO.getItems().stream()
                                        .map(dto -> OrderItem.builder()
                                                            .orderId(orderId)
                                                            .productId(dto.getProductId())
                                                            .quantity(dto.getQuantity())
                                                            .unitPrice(dto.getPrice())
                                                            .build())
                                        .toList();

        order.setItems(items);
        orderRepository.save(order);

        String itemsDescription = order.getItems().stream()
                                          .map(item -> String.format("{productId=%s, quantity=%d, unitPrice=%.2f}",
                                                  item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                                          .collect(Collectors.joining(", "));
        log.info("""
                        Order placed successfully
                        id={},
                        userId={},
                        cartId={},
                        status={},
                        totalAmount={},
                        items=[{}]""",
                order.getId(),
                order.getUserId(),
                order.getCartId(),
                order.getStatus(),
                order.getTotalAmount(),
                itemsDescription);
    }
}
