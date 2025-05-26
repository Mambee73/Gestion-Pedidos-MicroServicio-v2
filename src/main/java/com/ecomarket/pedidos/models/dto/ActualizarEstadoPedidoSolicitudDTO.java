package com.ecomarket.pedidos.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoPedidoSolicitudDTO {
    @NotBlank(message = "El nuevo estado no puede estar vacío.")
    private String nuevoEstado; 
}
