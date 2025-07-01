package com.ecomarket.pedidos.models.dto;

import com.ecomarket.pedidos.models.entity.EstadoPedido;
import lombok.Data;



@Data
public class RespuestaResumenPedidoDTO {

    private Long id;
    private String fechaPedido;
    private EstadoPedido estado;
    private long montoTotal;
}
