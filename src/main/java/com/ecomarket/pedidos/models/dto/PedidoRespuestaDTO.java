package com.ecomarket.pedidos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRespuestaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaPedido;
    private String estado; // Representación en String del enum EstadoPedido
    private DireccionEnvioDTO direccionEnvio;
    private Double montoTotal;
    private List<ItemPedidoRespuestaDTO> items;
}
