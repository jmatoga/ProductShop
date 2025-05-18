package jm.cart_service.application.mapper;

import jm.cart_service.domain.model.CartItem;
import jm.common.dto.CartItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDTO mapToDTO(CartItem cartItem);
}
