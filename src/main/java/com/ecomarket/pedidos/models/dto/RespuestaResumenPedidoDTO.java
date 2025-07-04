package com.ecomarket.pedidos.models.dto;

import com.ecomarket.pedidos.models.entity.EstadoPedido;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RespuestaResumenPedidoDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;
    private long montoTotal;
}