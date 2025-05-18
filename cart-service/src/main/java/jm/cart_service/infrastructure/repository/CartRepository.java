package jm.cart_service.infrastructure.repository;

import jm.cart_service.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Query(value = "SELECT * FROM carts " +
                           "WHERE user_id = :userId AND status = 'ACTIVE' " +
                           "ORDER BY created_at DESC " +
                           "LIMIT 1", nativeQuery = true)
    Optional<Cart> findByUserId(@Param("userId") UUID userId);
}
