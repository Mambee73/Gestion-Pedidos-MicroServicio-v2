package com.ecomarket.pedidos.controlador;

import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.RespuestaResumenPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudActualizarEstadoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.servicios.ServicioPedido;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoControlador {

    @Autowired
    private ServicioPedido servicioPedido;

    @PostMapping
    public ResponseEntity<RespuestaPedidoDTO> crearPedido(@Valid @RequestBody SolicitudCrearPedidoDTO solicitud) {
        RespuestaPedidoDTO pedidoCreado = servicioPedido.crearPedido(solicitud);
        return new ResponseEntity<>(pedidoCreado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RespuestaResumenPedidoDTO>> obtenerTodosLosPedidos() {
        List<RespuestaResumenPedidoDTO> todosLosPedidos = servicioPedido.obtenerTodosLosPedidos();
        return ResponseEntity.ok(todosLosPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaPedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
        RespuestaPedidoDTO pedidoEncontrado = servicioPedido.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedidoEncontrado);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<RespuestaResumenPedidoDTO>> obtenerPedidosPorClienteId(@PathVariable Long clienteId) {
        List<RespuestaResumenPedidoDTO> resumenDePedidos = servicioPedido.obtenerPedidosPorClienteId(clienteId);
        return ResponseEntity.ok(resumenDePedidos);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<RespuestaPedidoDTO> actualizarEstadoPedido(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudActualizarEstadoDTO solicitud) {
        RespuestaPedidoDTO pedidoActualizado = servicioPedido.actualizarEstado(id, solicitud.getNuevoEstado());
        return ResponseEntity.ok(pedidoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        servicioPedido.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}