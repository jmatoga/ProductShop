package jm.product_service.infrastructure.repository;


import jm.product_service.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByCartIdAndProductId(UUID cartId, UUID productId);

    List<Reservation> findAllByCartId(UUID cartId);

    void deleteAllByCartId(UUID cartId);
}