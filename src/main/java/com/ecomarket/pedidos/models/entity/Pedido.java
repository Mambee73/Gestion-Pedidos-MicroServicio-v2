package com.ecomarket.pedidos.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(name = "direccion_calle")
    private String direccionCalle;

    @Column(name = "direccion_ciudad")
    private String direccionCiudad;

    @Column(nullable = false)
    private long montoTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itemsPedido = new ArrayList<>();
    
    public void agregarItemPedido(ItemPedido item) {
        this.itemsPedido.add(item);
        item.setPedido(this);
    }

    public void removerItemPedido(ItemPedido item) {
        this.itemsPedido.remove(item);
        item.setPedido(null);
    }
}