package com.ecomarket.pedidos.models.dto;

import jakarta.validation.Valid; 
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCrearPedidoDTO {

    @NotNull(message = "El ID del cliente no puede ser nulo.")
    private Long clienteId;

    @NotNull(message = "La dirección de envío no puede ser nula.")
    @Valid 
    private DireccionDTO direccionEnvio;

    @NotEmpty(message = "La lista de ítems del pedido no puede estar vacía.")
    @Valid 
    private List<SolicitudItemPedidoDTO> items;
}

