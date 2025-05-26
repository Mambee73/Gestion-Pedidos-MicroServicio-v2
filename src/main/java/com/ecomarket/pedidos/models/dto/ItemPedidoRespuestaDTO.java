package com.ecomarket.pedidos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRespuestaDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto; 
    private int cantidad;
    private Double precioAlComprar;
    private Double subTotal;
}

