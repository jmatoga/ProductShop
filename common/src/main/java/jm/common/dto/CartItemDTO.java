package jm.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CartItemDTO {
    @NotBlank
    private UUID id;

    @NotBlank
    private UUID cartId;

    @NotBlank
    private UUID productId;

    @Min(0)
    private int quantity;

    @Min(0)
    private double price;
}
