package com.ecomarket.pedidos.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoSolicitudDTO {

    @NotNull(message = "El ID del producto no puede ser nulo.")
    private Long productoId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1.") //
    private int cantidad;
}

