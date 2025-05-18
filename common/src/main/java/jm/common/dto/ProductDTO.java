package jm.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank
    private UUID id;

    @NotBlank
    private String name;

    @Min(0)
    private int availableQuantity;

    @Min(0)
    private double price;
}
