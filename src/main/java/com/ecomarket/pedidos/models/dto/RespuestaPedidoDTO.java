package com.ecomarket.pedidos.models.dto;

import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Relation(collectionRelation = "pedidos", itemRelation = "pedido")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaPedidoDTO extends RepresentationModel<RespuestaPedidoDTO> {
    
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;
    private DireccionDTO direccionEnvio;
    private long montoTotal;
    private List<RespuestaItemPedidoDTO> items;
}