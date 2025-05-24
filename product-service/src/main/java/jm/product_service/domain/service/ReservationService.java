package jm.product_service.domain.service;

import jm.product_service.domain.model.Reservation;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    void addOrUpdateReservation(UUID cartId, UUID productId, int incomingQuantity);

    void removeQuantityFromReservation(UUID cartId, UUID productId, int incomingQuantity);

    List<Reservation> getReservationsByCart(UUID cartId);

    void deleteAllByCart(UUID cartId);
}
