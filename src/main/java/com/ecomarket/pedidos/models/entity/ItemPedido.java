package com.ecomarket.pedidos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "items_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Column(nullable = false)
    private Long productoId;

    @Size(max = 255, message = "El nombre del producto no debe exceder los 200 caracteres.")
    @Column(length = 200)
    private String nombreProducto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    @Column(nullable = false)
    private int cantidad;

    @NotNull(message = "El precio al comprar no puede ser nulo.")
    @Column(nullable = false)
    private Double precioAlComprar;

    @NotNull(message = "El subtotal no puede ser nulo.")
    @Column(nullable = false)
    private Double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @NotNull
    private Pedido pedido;
}

