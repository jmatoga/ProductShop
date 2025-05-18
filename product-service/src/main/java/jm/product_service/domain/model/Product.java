package jm.product_service.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Min(0)
    private double price;

    @Column(nullable = false)
    @Min(0)
    private int quantity;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    public void decreaseQuantity(int incomingQuantity) {
        checkQuantity(incomingQuantity);
        if (quantity < incomingQuantity)
            throw new IllegalStateException("Not enough stock");
        quantity -= incomingQuantity;
        updatedAt = Instant.now();
    }

    public void increaseQuantity(int incomingQuantity) {
        checkQuantity(incomingQuantity);
        quantity += incomingQuantity;
        updatedAt = Instant.now();
    }

    private void checkQuantity(int incomingQuantity) {
        if (incomingQuantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
    }
}
