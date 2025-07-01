package com.ecomarket.pedidos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaItemPedidoDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto; 
    private int cantidad;
    private long precioAlComprar;
    private long subTotal;
}

