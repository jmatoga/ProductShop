package jm.product_service.application.mapper;

import jm.common.dto.ProductDTO;
import jm.product_service.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO mapToDTO(Product productDTO);
}
