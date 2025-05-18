package jm.product_service.controller;

import jm.common.dto.ProductDTO;
import jm.product_service.application.mapper.ProductMapper;
import jm.product_service.application.product.query.GetProductQuery;
import jm.product_service.application.product.query.GetProductQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final GetProductQueryHandler queryHandler;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") UUID id) {
        ProductDTO dto = productMapper.mapToDTO(queryHandler.handle(new GetProductQuery(id)));
        log.info("""
                Received product details:
                ID: {}
                Name: {}
                Available Quantity: {}
                Price: {} PLN""",
                dto.getId(),
                dto.getName(),
                dto.getAvailableQuantity(),
                dto.getPrice());
        return ResponseEntity.ok(dto);
    }
}
