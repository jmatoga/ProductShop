package jm.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    @NotBlank
    private UUID id;

    @NotBlank
    private UUID userId;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    private List<CartItemDTO> items = new ArrayList<>();
}
