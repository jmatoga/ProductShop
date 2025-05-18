package jm.cart_service.infrastructure.messaging;

public interface EventPublisher {
    void publish(Object domainEvent); // Publikuje dowolne zdarzenie domenowe do brokera
}
