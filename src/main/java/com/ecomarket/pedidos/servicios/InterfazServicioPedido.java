package com.ecomarket.pedidos.servicios;

import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaResumenPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudActualizarEstadoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.models.entity.EstadoPedido;

import java.util.List;


public interface InterfazServicioPedido {

    RespuestaPedidoDTO crearPedido(SolicitudCrearPedidoDTO solicitud);

    RespuestaPedidoDTO obtenerPedidoPorId(Long id);

    List<RespuestaResumenPedidoDTO> obtenerPedidosPorClienteId(Long clienteId);

    RespuestaPedidoDTO actualizarEstado(Long id, EstadoPedido nuevoEstado);

    void eliminarPedido(Long id);
}