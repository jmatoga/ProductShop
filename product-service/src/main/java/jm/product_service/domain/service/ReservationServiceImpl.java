package jm.product_service.domain.service;

import jakarta.transaction.Transactional;
import jm.product_service.domain.model.Reservation;
import jm.product_service.infrastructure.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public void addOrUpdateReservation(UUID cartId, UUID productId, int incomingQuantity) {
        Optional<Reservation> reservation = getReservation(cartId, productId);
        if (reservation.isPresent()) {
            reservation.get().increaseQuantity(incomingQuantity);
        } else {
            reservationRepository.save(new Reservation(cartId, productId, incomingQuantity));
        }
    }
    @Override
    @Transactional
    public void removeQuantityFromReservation(UUID cartId, UUID productId, int incomingQuantity) {
        Reservation reservation = getReservation(cartId, productId)
                        .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.decreaseQuantity(incomingQuantity);
        if (reservation.getQuantity() <= 0) {
            reservationRepository.delete(reservation);
        }
    }

    @Override
    public Optional<Reservation> getReservation(UUID cartId, UUID productId) {
        return reservationRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Override
    public List<Reservation> getReservationsByCart(UUID cartId) {
        return reservationRepository.findAllByCartId(cartId);
    }

    @Override
    public void deleteAllByCart(UUID cartId) {
        reservationRepository.deleteAllByCartId(cartId);
    }
}
