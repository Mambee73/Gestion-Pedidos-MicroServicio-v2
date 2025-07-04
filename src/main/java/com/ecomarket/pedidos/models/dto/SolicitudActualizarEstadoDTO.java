package com.ecomarket.pedidos.models.dto;

import com.ecomarket.pedidos.models.entity.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudActualizarEstadoDTO {
    @NotNull(message = "El nuevo estado no puede estar vacío.")
    private EstadoPedido nuevoEstado; 
}
