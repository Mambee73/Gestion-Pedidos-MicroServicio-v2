package com.ecomarket.pedidos.servicios;

import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaResumenPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioPedido implements InterfazServicioPedido {

    // Inyectamos las dependencias que necesitamos para trabajar
    private final RepositorioPedido Repositoriopedido;
    // Más adelante crearemos este Mapper para las conversiones
    // private final PedidoMapper pedidoMapper; 

    @Override
    @Transactional
    public RespuestaPedidoDTO crearPedido(SolicitudCrearPedidoDTO solicitud) {
        // TODO: Implementar la lógica para crear un pedido.
        // 1. Convertir DTO a Entidad.
        // 2. Realizar cálculos (monto total).
        // 3. Guardar en la BD usando el repositorio.
        // 4. Convertir la Entidad guardada a DTO de respuesta.
        return null; 
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaPedidoDTO obtenerPedidoPorId(Long id) {
        // TODO: Implementar la lógica para buscar un pedido.
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaResumenPedidoDTO> obtenerPedidosPorClienteId(Long clienteId) {
        // TODO: Implementar la lógica.
        return null;
    }

    @Override
    @Transactional
    public RespuestaPedidoDTO actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        // TODO: Implementar la lógica.
        return null;
    }

    @Override
    @Transactional
    public void eliminarPedido(Long id) {
        // TODO: Implementar la lógica.
    }
}