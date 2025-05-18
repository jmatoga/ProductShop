package jm.order_service.application.mapper;

import jm.common.dto.CartItemDTO;
import jm.order_service.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity",  target = "quantity")
    @Mapping(source = "price", target = "unitPrice")
    OrderItem mapToDTO(CartItemDTO cartItemDTO);
}
