package com.ecomarket.pedidos.controlador;

import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaResumenPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudActualizarEstadoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.servicios.ServicioPedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; 


@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor 
public class PedidoControlador {
    private final ServicioPedido servicioPedido;
}
// POST /api/v1/pedidos
@PostMapping
public ResponseEntity<RespuestaPedidoDTO> crearPedido(@Valid @RequestBody SolicitudCrearPedidoDTO solicitud) {

    RespuestaPedidoDTO pedidoCreado = servicioPedido.crearPedido(solicitud);
    return new ResponseEntity<>(pedidoCreado, HttpStatus.CREATED);
}

// GET /api/v1/pedidos/{id}
@GetMapping("/{id}")
public ResponseEntity<RespuestaPedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
    RespuestaPedidoDTO pedidoEncontrado = servicioPedido.obtenerPedidoPorId(id);
    return ResponseEntity.ok(pedidoEncontrado);
}

// GET /api/v1/pedidos/cliente/{clienteId}
@GetMapping("/cliente/{clienteId}")
public ResponseEntity<List<RespuestaResumenPedidoDTO>> obtenerPedidosPorClienteId(@PathVariable Long clienteId) {
    List<RespuestaResumenPedidoDTO> resumenDePedidos = servicioPedido.obtenerPedidosPorClienteId(clienteId);
    return ResponseEntity.ok(resumenDePedidos);
}

// PUT /api/v1/pedidos/{id}/estado
@PutMapping("/{id}/estado")
public ResponseEntity<RespuestaPedidoDTO> actualizarEstadoPedido(
        @PathVariable Long id,
        @Valid @RequestBody SolicitudActualizarEstadoDTO solicitud) {
    RespuestaPedidoDTO pedidoActualizado = servicioPedido.actualizarEstado(id, solicitud.getNuevoEstado());
    return ResponseEntity.ok(pedidoActualizado);
}

// DELETE /api/v1/pedidos/{id}
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
    servicioPedido.eliminarPedido(id);
    return ResponseEntity.noContent().build();
}