package jm.product_service.domain.service;

import jakarta.transaction.Transactional;
import jm.product_service.domain.model.Product;
import jm.product_service.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void decreaseStock(UUID productId, int incomingQuantity) {
        Product product = getProduct(productId);
        product.decreaseQuantity(incomingQuantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void increaseStock(UUID productId, int incomingQuantity) {
        Product product = getProduct(productId);
        product.increaseQuantity(incomingQuantity);
        productRepository.save(product);
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                       .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
