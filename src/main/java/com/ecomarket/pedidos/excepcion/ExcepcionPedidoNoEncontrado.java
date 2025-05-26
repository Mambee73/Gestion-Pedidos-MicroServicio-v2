package com.ecomarket.pedidos.excepcion;

public class ExcepcionPedidoNoEncontrado extends RuntimeException {
    public ExcepcionPedidoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
    
