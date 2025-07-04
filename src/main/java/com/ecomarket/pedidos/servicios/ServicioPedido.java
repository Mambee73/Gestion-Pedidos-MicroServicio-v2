package com.ecomarket.pedidos.servicios;

import com.ecomarket.pedidos.assembler.PedidoModelAssembler;
import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaResumenPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudItemPedidoDTO;
import com.ecomarket.pedidos.excepcion.ExcepcionPedidoNoEncontrado;
import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.ecomarket.pedidos.models.entity.ItemPedido;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioPedido {

    @Autowired
    private RepositorioPedido repositorioPedido;

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Transactional
    public RespuestaPedidoDTO crearPedido(SolicitudCrearPedidoDTO solicitud) {
        Pedido pedidoNuevo = solicitudToEntidad(solicitud);
        Pedido pedidoGuardado = repositorioPedido.save(pedidoNuevo);
        return pedidoModelAssembler.toModel(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public RespuestaPedidoDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = repositorioPedido.findById(id)
                .orElseThrow(() -> new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id));
        return pedidoModelAssembler.toModel(pedido);
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
        return pedidoModelAssembler.toModel(pedidoActualizado);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        if (!repositorioPedido.existsById(id)) {
            throw new ExcepcionPedidoNoEncontrado("No se puede eliminar, pedido no encontrado con ID: " + id);
        }
        repositorioPedido.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RespuestaResumenPedidoDTO> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = repositorioPedido.findAll();
        return pedidos.stream()
                .map(this::entidadToResumenDTO)
                .collect(Collectors.toList());
    }

    private Pedido solicitudToEntidad(SolicitudCrearPedidoDTO solicitud) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(solicitud.getClienteId());

        if (solicitud.getDireccionEnvio() != null) {
            pedido.setDireccionCalle(solicitud.getDireccionEnvio().getCalle());
            pedido.setDireccionCiudad(solicitud.getDireccionEnvio().getCiudad());
        }

        long montoTotal = 0L;
        if (solicitud.getItems() != null) {
            for (SolicitudItemPedidoDTO itemDto : solicitud.getItems()) {
                ItemPedido item = new ItemPedido();
                item.setProductoId(itemDto.getProductoId());
                item.setCantidad(itemDto.getCantidad());
                long precioTemporal = 5000L;
                item.setPrecioAlComprar(precioTemporal);
                item.setNombreProducto("Producto Temporal " + itemDto.getProductoId());
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

    private RespuestaResumenPedidoDTO entidadToResumenDTO(Pedido pedido) {
        RespuestaResumenPedidoDTO dto = new RespuestaResumenPedidoDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setMontoTotal(pedido.getMontoTotal());
        return dto;
    }
}