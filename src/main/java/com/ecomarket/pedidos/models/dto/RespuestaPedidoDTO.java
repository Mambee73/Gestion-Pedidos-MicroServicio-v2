package com.ecomarket.pedidos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaPedidoDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaPedido;
    private String estado; // Representación en String del enum EstadoPedido
    private DireccionDTO direccionEnvio;
    private long montoTotal;
    private List<RespuestaItemPedidoDTO> items;
}
