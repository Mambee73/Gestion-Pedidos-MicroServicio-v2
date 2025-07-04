package com.ecomarket.pedidos.assembler;

import com.ecomarket.pedidos.controlador.PedidoControlador;
import com.ecomarket.pedidos.models.dto.DireccionDTO;
import com.ecomarket.pedidos.models.dto.RespuestaItemPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.entity.Pedido;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, RespuestaPedidoDTO> {

    @Override
    public RespuestaPedidoDTO toModel(Pedido pedido) {
        RespuestaPedidoDTO dto = new RespuestaPedidoDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setMontoTotal(pedido.getMontoTotal());

        if (pedido.getDireccionCalle() != null) {
            DireccionDTO direccionDTO = new DireccionDTO();
            direccionDTO.setCalle(pedido.getDireccionCalle());
            direccionDTO.setCiudad(pedido.getDireccionCiudad());
            dto.setDireccionEnvio(direccionDTO);
        }

        if (pedido.getItemsPedido() != null) {
            dto.setItems(pedido.getItemsPedido().stream().map(item -> {
                RespuestaItemPedidoDTO itemDto = new RespuestaItemPedidoDTO();
                itemDto.setId(item.getId());
                itemDto.setProductoId(item.getProductoId());
                itemDto.setNombreProducto(item.getNombreProducto());
                itemDto.setCantidad(item.getCantidad());
                itemDto.setPrecioAlComprar(item.getPrecioAlComprar());
                itemDto.setSubTotal(item.getSubTotal());
                return itemDto;
            }).collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        try {
            dto.add(linkTo(methodOn(PedidoControlador.class).obtenerPedidoPorId(pedido.getId())).withSelfRel());
            dto.add(linkTo(methodOn(PedidoControlador.class).actualizarEstadoPedido(pedido.getId(), null)).withRel("actualizarEstado"));
        } catch (Exception e) {
            System.err.println("Error al crear los enlaces HATEOAS para el pedido " + pedido.getId());
        }

        return dto;
    }
}