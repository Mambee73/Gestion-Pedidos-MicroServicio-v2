package com.ecomarket.pedidos.models.dto;

import com.ecomarket.pedidos.models.entity.EstadoPedido;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudActualizarEstadoDTO {
    @NotBlank(message = "El nuevo estado no puede estar vacío.")
    private EstadoPedido nuevoEstado; 
}
