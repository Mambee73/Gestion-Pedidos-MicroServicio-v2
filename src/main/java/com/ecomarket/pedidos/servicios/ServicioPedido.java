package com.ecomarket.pedidos.servicios;

import com.ecomarket.pedidos.models.dto.*;
import com.ecomarket.pedidos.excepcion.ExcepcionPedidoNoEncontrado;
import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.ecomarket.pedidos.models.entity.ItemPedido;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioPedido {

    @Autowired
    private RepositorioPedido repositorioPedido;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public RespuestaPedidoDTO crearPedido(SolicitudCrearPedidoDTO solicitud) {
        Pedido pedidoNuevo = solicitudToEntidad(solicitud);
        Pedido pedidoGuardado = repositorioPedido.save(pedidoNuevo);
        return entidadToRespuestaDTO(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public RespuestaPedidoDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = repositorioPedido.findById(id)
                .orElseThrow(() -> new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id));
        return entidadToRespuestaDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<RespuestaResumenPedidoDTO> obtenerPedidosPorClienteId(Long clienteId) {
        List<Pedido> pedidos = repositorioPedido.findByClienteIdOrderByFechaPedidoDesc(clienteId);
        return pedidos.stream()
                .map(this::entidadToResumenDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RespuestaPedidoDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedidoExistente = repositorioPedido.findById(id)
                .orElseThrow(() -> new ExcepcionPedidoNoEncontrado("No se puede actualizar, pedido no encontrado con ID: " + id));
        pedidoExistente.setEstado(nuevoEstado);
        Pedido pedidoActualizado = repositorioPedido.save(pedidoExistente);
        return entidadToRespuestaDTO(pedidoActualizado);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (!repositorioPedido.existsById(id)) {
            throw new ExcepcionPedidoNoEncontrado("No se puede eliminar, pedido no encontrado con ID: " + id);
        }
        repositorioPedido.deleteById(id);
    }





    private Pedido solicitudToEntidad(SolicitudCrearPedidoDTO solicitud) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(solicitud.getClienteId());

        try {
            if (solicitud.getDireccionEnvio() != null) {
                pedido.setDireccionEnvioJson(objectMapper.writeValueAsString(solicitud.getDireccionEnvio()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar la dirección de envío.", e);
        }

        long montoTotal = 0L;
        if (solicitud.getItems() != null) {
            for (SolicitudItemPedidoDTO itemDto : solicitud.getItems()) {
                ItemPedido item = new ItemPedido();
                item.setProductoId(itemDto.getProductoId());
                item.setCantidad(itemDto.getCantidad());
                long precioTemporal = 5000L;
                item.setPrecioAlComprar(precioTemporal);
                item.setNombreProducto("Producto " + itemDto.getProductoId());
                long subTotal = precioTemporal * itemDto.getCantidad();
                item.setSubTotal(subTotal);
                montoTotal += subTotal;
                pedido.agregarItemPedido(item); 
            }
        }
        
        pedido.setMontoTotal(montoTotal);
        pedido.setEstado(EstadoPedido.PROCESANDO);

        return pedido;
    }
    
    private RespuestaPedidoDTO entidadToRespuestaDTO(Pedido pedido) {
        RespuestaPedidoDTO dto = new RespuestaPedidoDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setMontoTotal(pedido.getMontoTotal());
        
        try {
            if (pedido.getDireccionEnvioJson() != null && !pedido.getDireccionEnvioJson().isEmpty()) {
                dto.setDireccionEnvio(objectMapper.readValue(pedido.getDireccionEnvioJson(), DireccionDTO.class));
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error D" + pedido.getId());
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

        return dto;
    }

    private RespuestaResumenPedidoDTO entidadToResumenDTO(Pedido pedido) {
        RespuestaResumenPedidoDTO dto = new RespuestaResumenPedidoDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setMontoTotal(pedido.getMontoTotal());
        return dto;
    }
}
