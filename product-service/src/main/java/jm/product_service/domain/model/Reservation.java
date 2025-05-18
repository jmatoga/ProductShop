package jm.product_service.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID cartId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    public Reservation(UUID cartId, UUID productId, int incomingQuantity) {
        checkQuantity(incomingQuantity);
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = incomingQuantity;
        this.createdAt = Instant.now();
    }

    public void decreaseQuantity(int incomingQuantity) {
        checkQuantity(incomingQuantity);
        if (quantity < incomingQuantity)
            throw new IllegalStateException("Not enough stock");
        quantity -= incomingQuantity;
    }

    public void increaseQuantity(int incomingQuantity) {
        checkQuantity(incomingQuantity);
        quantity += incomingQuantity;
    }

    private void checkQuantity(int incomingQuantity) {
        if (incomingQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
