package jm.product_service.application.reservation.command.handler;

import jakarta.transaction.Transactional;
import jm.product_service.application.reservation.command.ReleaseProductCommand;
import jm.product_service.domain.service.ProductService;
import jm.product_service.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReleaseProductCommandHandler {
    private final ProductService productService;
    private final ReservationService reservationService;

    @Transactional
    public void handle(ReleaseProductCommand cmd) {
        reservationService.removeQuantityFromReservation(cmd.cartId(), cmd.productId(), cmd.quantity());
        productService.increaseStock(cmd.productId(), cmd.quantity());
    }
}
