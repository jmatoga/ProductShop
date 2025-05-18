package jm.product_service.application.product.query;

import jm.product_service.domain.model.Product;
import jm.product_service.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProductQueryHandler {
    private final ProductRepository productRepository;

    public Product handle(GetProductQuery query) {
        return productRepository.findById(query.productId())
                       .orElseThrow(() -> new IllegalArgumentException("Product with id: " + query.productId() + " not found"));
    }
}
