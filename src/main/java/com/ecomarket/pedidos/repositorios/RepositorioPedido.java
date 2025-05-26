package com.ecomarket.pedidos.repositorios;

import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.models.entity.EstadoPedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepositorioPedido extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);
    
    List<Pedido> findByEstado(EstadoPedido estado);
}