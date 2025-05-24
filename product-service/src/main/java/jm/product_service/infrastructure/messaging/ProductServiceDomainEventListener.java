package jm.product_service.infrastructure.messaging;

import jm.common.dto.ProductDTO;
import jm.common.event.ProductRemovedEvent;
import jm.common.event.ProductReservedEvent;
import jm.product_service.application.mapper.ProductMapper;
import jm.product_service.application.product.query.GetProductQuery;
import jm.product_service.application.product.query.GetProductQueryHandler;
import jm.product_service.application.reservation.command.ReleaseProductCommand;
import jm.product_service.application.reservation.command.ReserveProductCommand;
import jm.product_service.application.reservation.command.handler.ReleaseProductCommandHandler;
import jm.product_service.application.reservation.command.handler.ReserveProductCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

import static jm.product_service.infrastructure.config.RabbitMqConfig.PRODUCT_REMOVED_QUEUE;
import static jm.product_service.infrastructure.config.RabbitMqConfig.PRODUCT_RESERVED_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceDomainEventListener {
    private final ReserveProductCommandHandler reserveHandler;
    private final ReleaseProductCommandHandler releaseProductHandler;
    private final GetProductQueryHandler queryHandler;
    private final ProductMapper productMapper;

    @RabbitListener(queues = PRODUCT_RESERVED_QUEUE)
    public UUID handleProductReserved(ProductReservedEvent event) {
        log.info("Handling ProductReservedEvent for productId={}, quantity={}, cartId={}", event.productId(), event.quantity(), event.cartId());
        reserveHandler.handle(new ReserveProductCommand(event.productId(), event.cartId(), event.quantity()));
        ProductDTO product = productMapper.mapToDTO(queryHandler.handle(new GetProductQuery(event.productId())));
        log.info("Reserved product={}, quantity={}, price={}", event.productId(), event.quantity(), product.getPrice());
        return event.cartItemId();
    }

    @RabbitListener(queues = PRODUCT_REMOVED_QUEUE)
    public void handleProductRemoved(ProductRemovedEvent event) {
        log.info("Handling ProductRemovedEvent for productId={}, quantity={}, cartId={}", event.productId(), event.quantity(), event.cartId());
        releaseProductHandler.handle(new ReleaseProductCommand(event.productId(), event.cartId(), event.quantity()));
        ProductDTO product = productMapper.mapToDTO(queryHandler.handle(new GetProductQuery(event.productId())));
        log.info("Deleted product={}, quantity={}, price={}", event.productId(), event.quantity(), product.getPrice());
    }
}
