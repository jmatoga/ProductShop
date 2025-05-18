package jm.cart_service.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID cartId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    @Min(0)
    private int quantity;

    @Column(nullable = false)
    @Min(0)
    private double price;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt = Instant.now();

    public void increaseQuantity(int incomingQuantity) {
        this.quantity += incomingQuantity;
    }

    public void decreaseQuantity(int incomingQuantity) {
        this.quantity -= incomingQuantity;
    }
}
