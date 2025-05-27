package com.ecomarket.pedidos.controlador;

import com.ecomarket.pedidos.models.dto.CrearPedidoSolicitudDTO;
import com.ecomarket.pedidos.models.dto.PedidoRespuestaDTO;
import com.ecomarket.pedidos.models.dto.ActualizarEstadoPedidoSolicitudDTO;
import com.ecomarket.pedidos.models.dto.DireccionEnvioDTO;
import com.ecomarket.pedidos.models.dto.ItemPedidoRespuestaDTO;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.models.entity.ItemPedido;
import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.ecomarket.pedidos.servicios.ServicioPedido;
import com.ecomarket.pedidos.excepcion.ExcepcionPedidoNoEncontrado;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
public class ControladorPedido {

    private final ServicioPedido servicioPedido;
    private final ObjectMapper objectMapper;

    @Autowired
    public ControladorPedido(ServicioPedido servicioPedido, ObjectMapper objectMapper) {
        this.servicioPedido = servicioPedido;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<PedidoRespuestaDTO> crearPedido(@Valid @RequestBody CrearPedidoSolicitudDTO solicitudCreacion) {
        Pedido pedidoEntidad = new Pedido();
        pedidoEntidad.setClienteId(solicitudCreacion.getClienteId());
        try {
            if (solicitudCreacion.getDireccionEnvio() != null) {
                pedidoEntidad.setDireccionEnvioJson(objectMapper.writeValueAsString(solicitudCreacion.getDireccionEnvio()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar la dirección de envío.", e);
        }
        pedidoEntidad.setItemsPedido(new ArrayList<>());
        if (solicitudCreacion.getItems() != null) {
            double montoTotalCalculado = 0.0;
            for (com.ecomarket.pedidos.models.dto.ItemPedidoSolicitudDTO itemDto : solicitudCreacion.getItems()) {
                ItemPedido itemEntidad = new ItemPedido();
                itemEntidad.setProductoId(itemDto.getProductoId());
                itemEntidad.setCantidad(itemDto.getCantidad());
                itemEntidad.setPrecioAlComprar(50.0); 
                itemEntidad.setNombreProducto("Producto " + itemDto.getProductoId()); 
                itemEntidad.setSubTotal(itemEntidad.getPrecioAlComprar() * itemEntidad.getCantidad());
                pedidoEntidad.agregarItemPedido(itemEntidad);
                montoTotalCalculado += itemEntidad.getSubTotal();
            }
            pedidoEntidad.setMontoTotal(montoTotalCalculado);
        }
        pedidoEntidad.setEstado(EstadoPedido.PROCESANDO);
        Pedido pedidoGuardado = servicioPedido.crear_ActualizarPedido(pedidoEntidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirAPedidoRespuestaDTO(pedidoGuardado));
    }

    @GetMapping
    public ResponseEntity<List<PedidoRespuestaDTO>> obtenerTodosLosPedidos() {
        List<Pedido> pedidosEntidad = servicioPedido.obtenerTodosLosPedidos();
        List<PedidoRespuestaDTO> pedidosDTO = pedidosEntidad.stream()
                                             .map(this::convertirAPedidoRespuestaDTO)
                                             .collect(Collectors.toList());
        return ResponseEntity.ok(pedidosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoRespuestaDTO> obtenerPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedidoOptional = servicioPedido.obtenerPedidoPorId(id);
        if (pedidoOptional.isPresent()) {
            return ResponseEntity.ok(convertirAPedidoRespuestaDTO(pedidoOptional.get()));
        } else {
            throw new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id);
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoRespuestaDTO>> obtenerPedidosPorClienteId(@PathVariable Long clienteId) {
        List<Pedido> pedidosEntidad = servicioPedido.obtenerPedidosPorClienteId(clienteId);
        List<PedidoRespuestaDTO> pedidosDTO = pedidosEntidad.stream()
                                             .map(this::convertirAPedidoRespuestaDTO)
                                             .collect(Collectors.toList());
        return ResponseEntity.ok(pedidosDTO);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoRespuestaDTO> actualizarEstadoPedido(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPedidoSolicitudDTO solicitudActualizacion) {
        Optional<Pedido> pedidoOptional = servicioPedido.obtenerPedidoPorId(id);
        if (!pedidoOptional.isPresent()) {
            throw new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id);
        }
        Pedido pedidoExistente = pedidoOptional.get();
        try {
            pedidoExistente.setEstado(EstadoPedido.valueOf(solicitudActualizacion.getNuevoEstado().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de pedido inválido: " + solicitudActualizacion.getNuevoEstado());
        }
        Pedido pedidoActualizadoEnServicio = servicioPedido.crear_ActualizarPedido(pedidoExistente);
        return ResponseEntity.ok(convertirAPedidoRespuestaDTO(pedidoActualizadoEnServicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoRespuestaDTO> actualizarDatosGeneralesPedido(
            @PathVariable Long id,
            @Valid @RequestBody CrearPedidoSolicitudDTO solicitudActualizacion) {
        Optional<Pedido> pedidoOptional = servicioPedido.obtenerPedidoPorId(id);
        if (!pedidoOptional.isPresent()) {
            throw new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id);
        }
        Pedido pedidoExistente = pedidoOptional.get();
        if(solicitudActualizacion.getClienteId() != null) {
            pedidoExistente.setClienteId(solicitudActualizacion.getClienteId());
        }
        try {
            if (solicitudActualizacion.getDireccionEnvio() != null) {
                pedidoExistente.setDireccionEnvioJson(objectMapper.writeValueAsString(solicitudActualizacion.getDireccionEnvio()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar la dirección de envío para actualización.", e);
        }
        Pedido pedidoActualizadoEnServicio = servicioPedido.crear_ActualizarPedido(pedidoExistente);
        return ResponseEntity.ok(convertirAPedidoRespuestaDTO(pedidoActualizadoEnServicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        servicioPedido.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoRespuestaDTO convertirAPedidoRespuestaDTO(Pedido pedido) {
        PedidoRespuestaDTO dto = new PedidoRespuestaDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setFechaPedido(pedido.getFechaPedido());
        if (pedido.getEstado() != null) {
            dto.setEstado(pedido.getEstado().name());
        }
        try {
            if (pedido.getDireccionEnvioJson() != null && !pedido.getDireccionEnvioJson().isEmpty()) {
                dto.setDireccionEnvio(objectMapper.readValue(pedido.getDireccionEnvioJson(), DireccionEnvioDTO.class));
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error al deserializar dirección para pedido ID " + pedido.getId() + ": " + e.getMessage());
        }
        dto.setMontoTotal(
            pedido.getMontoTotal() != null ? pedido.getMontoTotal().doubleValue() : null
        );
        if (pedido.getItemsPedido() != null) {
            dto.setItems(pedido.getItemsPedido().stream()
                    .map(this::convertirAItemPedidoRespuestaDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private ItemPedidoRespuestaDTO convertirAItemPedidoRespuestaDTO(ItemPedido item) {
        ItemPedidoRespuestaDTO dto = new ItemPedidoRespuestaDTO();
        dto.setId(item.getId());
        dto.setProductoId(item.getProductoId());
        dto.setNombreProducto(item.getNombreProducto());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioAlComprar(item.getPrecioAlComprar());
        dto.setSubTotal(item.getSubTotal());
        return dto;
    }
}