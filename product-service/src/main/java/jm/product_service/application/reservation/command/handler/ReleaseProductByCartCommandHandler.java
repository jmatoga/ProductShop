package jm.product_service.application.reservation.command.handler;

import jakarta.transaction.Transactional;
import jm.product_service.application.reservation.command.ReleaseProductByCartCommand;
import jm.product_service.domain.model.Reservation;
import jm.product_service.domain.service.ProductService;
import jm.product_service.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReleaseProductByCartCommandHandler {
    private final ReservationService reservationService;
    private final ProductService productService;

    @Transactional
    public void handle(ReleaseProductByCartCommand cmd) {
        List<Reservation> reservations = reservationService.getReservationsByCart(cmd.cartId());
        reservations.forEach(reservation -> productService.increaseStock(reservation.getProductId(), reservation.getQuantity()));
        reservationService.deleteAllByCart(cmd.cartId());
    }
}
