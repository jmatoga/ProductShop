package jm.cart_service.application.mapper;

import jm.cart_service.domain.model.Cart;
import jm.common.dto.CartDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    CartDTO mapToDTO(Cart cart);
}
