package jm.product_service.infrastructure.repository;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jm.product_service.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // Hibernate zakłada blokadę zapisu na tym wierszu do zakończenia transakcji
    @Transactional // Transakcja jest wymagana do Locka
    @Query("SELECT p FROM Product p WHERE p.id = :id") // Query jest wymagane do Locka
    Optional<Product> findByIdForUpdate(@Param("id") UUID id);
}
