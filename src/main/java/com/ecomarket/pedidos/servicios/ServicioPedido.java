package com.ecomarket.pedidos.servicios;

import com.ecomarket.pedidos.excepcion.ExcepcionPedidoNoEncontrado;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioPedido {

    private static final Logger logger = LoggerFactory.getLogger(ServicioPedido.class);

    private final RepositorioPedido repositorioPedido;

    @Autowired
    public ServicioPedido(RepositorioPedido repositorioPedido) {
        this.repositorioPedido = repositorioPedido;
    }

    @Transactional
    public Pedido crear_ActualizarPedido(Pedido pedido) {
        logger.info("Servicio: Guardando pedido para cliente ID: {}", pedido.getClienteId());
        return repositorioPedido.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosLosPedidos() {
        logger.info("Servicio: Obteniendo todos los pedidos.");
        return repositorioPedido.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        logger.info("Servicio: Obteniendo pedido con ID: {}", id);
        return repositorioPedido.findById(id);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        logger.info("Servicio: Eliminando pedido con ID: {}", id);
            if (!repositorioPedido.existsById(id)) {
                throw new ExcepcionPedidoNoEncontrado("Pedido no encontrado con ID: " + id);
            }
        repositorioPedido.deleteById(id);
        logger.info("Pedido ID {} eliminado (si existía).", id);
    }

    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorClienteId(Long clienteId) {
        logger.info("Servicio: Buscando pedidos para el cliente ID: {}", clienteId);
        return this.repositorioPedido.findByClienteIdOrderByFechaPedidoDesc(clienteId);
    }
}