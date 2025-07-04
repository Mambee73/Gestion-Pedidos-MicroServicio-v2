package com.ecomarket.pedidos;

import com.ecomarket.pedidos.models.entity.EstadoPedido;
import com.ecomarket.pedidos.models.entity.ItemPedido;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoaderPedidos implements CommandLineRunner {

    @Autowired
    private RepositorioPedido repositorioPedido;

    @Override
    public void run(String... args) throws Exception {
        if (repositorioPedido.count() > 0) {
            System.out.println("La base de datos ya contiene datos. DataLoader no se ejecutará.");
            return;
        }

        System.out.println("Iniciando carga de datos de ejemplo para Pedidos...");

        Faker faker = new Faker(new Locale("es", "CL"));
        Random random = new Random();

        for (int i = 1; i <= 3; i++) {
            Pedido pedido = new Pedido();
            pedido.setClienteId((long) i);
            pedido.setDireccionCalle(faker.address().streetAddress());
            pedido.setDireccionCiudad(faker.address().city());
            
            long montoTotal = 0L;
            int numeroDeItems = faker.number().numberBetween(1, 4);

            for (int j = 0; j < numeroDeItems; j++) {
                ItemPedido item = new ItemPedido();
                item.setProductoId((long) faker.number().numberBetween(100, 200));
                item.setNombreProducto(faker.commerce().productName());
                item.setCantidad(faker.number().numberBetween(1, 5));
                
                long precio = faker.number().numberBetween(1000, 15000);
                item.setPrecioAlComprar(precio);
                
                long subtotal = precio * item.getCantidad();
                item.setSubTotal(subtotal);

                montoTotal += subtotal;

                pedido.agregarItemPedido(item); 
            }
            
            pedido.setMontoTotal(montoTotal);

            EstadoPedido[] estados = EstadoPedido.values();
            pedido.setEstado(estados[random.nextInt(estados.length)]);

            repositorioPedido.save(pedido);
        }

        System.out.println("¡Datos de ejemplo cargados exitosamente!");
    }
}