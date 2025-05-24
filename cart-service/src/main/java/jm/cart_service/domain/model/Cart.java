package jm.cart_service.domain.model;

import jakarta.persistence.*;
import jm.cart_service.domain.event.CartExpiredEvent;
import jm.common.event.ProductRemovedEvent;
import jm.common.event.ProductReservedEvent;
import jm.common.event.CartCheckedOutEvent;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ECartStatus status = ECartStatus.ACTIVE;

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    @Transient // pomija pole w zapisie do bazy (zdarzenia domenowe publikowane za pomocą publishera na kolejkę)
    private final List<Object> domainEvents = new ArrayList<>();

    // fabryka
    public static Cart create(UUID userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        cart.status = ECartStatus.ACTIVE;
        cart.createdAt = Instant.now();
        cart.updatedAt = Instant.now();
        return cart;
    }

    public void addProduct(UUID productId, int quantity, double price) {
        ensureActive();
        CartItem existing = items.stream()
                                    .filter(item -> item.getProductId().equals(productId))
                                    .findFirst()
                                    .orElse(null);

        if (existing != null) {
            existing.increaseQuantity(quantity);
        } else {
            items.add(CartItem.builder()
                              .cartId(id)
                              .productId(productId)
                              .quantity(quantity)
                              .price(price)
                              .build());
        }
        this.updatedAt = Instant.now();
        // rejestracja zdarzenia
        this.domainEvents.add(new ProductReservedEvent(items.getLast().getId(), this.id, productId, quantity));
    }

    public void removeProduct(UUID productId, int quantity) {
        ensureActive();
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        else if (quantity == 0) {
            return;
        } else {
            domainEvents.add(new ProductRemovedEvent(this.id, productId, quantity));
        }
        this.updatedAt = Instant.now();
    }

    // finalizacja koszyka
    public void checkout() {
        ensureActive();
        this.status = ECartStatus.CHECKED_OUT;
        this.updatedAt = Instant.now();
        domainEvents.add(new CartCheckedOutEvent(this.id, this.userId));
    }

    private void ensureActive() {
        if (this.status != ECartStatus.ACTIVE) {
            throw new IllegalStateException("Cart is not active: " + status);
        }
    }

    // metoda czyszcząca listę domainEvents po publikacji
    public List<Object> pullDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public void expire() {
        if (this.status != ECartStatus.ACTIVE) {
            return;
        }
        this.status = ECartStatus.EXPIRED;
        this.updatedAt = Instant.now();
        this.domainEvents.add(new CartExpiredEvent(this.id, this.userId));
    }
}
