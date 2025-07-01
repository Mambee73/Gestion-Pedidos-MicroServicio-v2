package com.ecomarket.pedidos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data 
@NoArgsConstructor 
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del cliente no puede ser nulo.")
    @Column(nullable = false)
    private Long clienteId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @NotNull(message = "El estado del pedido no puede ser nulo.")
    private EstadoPedido estado;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String direccionEnvioJson;

    @NotNull(message = "El monto total no puede ser nulo.")
    @Column(nullable = false, precision = 10, scale = 2)
    private long montoTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) //????
    private List<ItemPedido> itemsPedido = new ArrayList<>();

    // MÉTODOS HELPER (Pedido esta en relacion a itemPedido)
    public void agregarItemPedido(ItemPedido item) {
        if (this.itemsPedido == null) { 
            this.itemsPedido = new ArrayList<>();
        }
        this.itemsPedido.add(item);
        item.setPedido(this);
    }

    public void removerItemPedido(ItemPedido item) {
        if (this.itemsPedido != null) {
            this.itemsPedido.remove(item);
            item.setPedido(null);
        }
    }

}