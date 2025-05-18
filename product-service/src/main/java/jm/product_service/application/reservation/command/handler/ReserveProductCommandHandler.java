package jm.product_service.application.reservation.command.handler;

import jakarta.transaction.Transactional;
import jm.product_service.application.reservation.command.ReserveProductCommand;
import jm.product_service.domain.model.Product;
import jm.product_service.domain.service.ReservationService;
import jm.product_service.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveProductCommandHandler {
    private final ReservationService reservationService;
    private final ProductRepository productRepository;

    @Transactional
    public void handle(ReserveProductCommand cmd) {
        Product product = productRepository.findByIdForUpdate(cmd.productId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.decreaseQuantity(cmd.quantity());
        reservationService.addOrUpdateReservation(cmd.cartId(), cmd.productId(), cmd.quantity());
        productRepository.save(product);
    }
}
