package com.ecomarket.pedidos.Servicio;

import com.ecomarket.pedidos.assembler.PedidoModelAssembler;
import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.models.entity.Pedido;
import com.ecomarket.pedidos.repositorios.RepositorioPedido;
import com.ecomarket.pedidos.servicios.ServicioPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServicioPedidoTest {

    @Mock
    private RepositorioPedido repositorioPedido;

    @Mock
    private PedidoModelAssembler pedidoModelAssembler;

    @InjectMocks
    private ServicioPedido servicioPedido;

    private Pedido pedidoEntidad;
    private SolicitudCrearPedidoDTO solicitudDTO;
    private RespuestaPedidoDTO respuestaDTO;

    @BeforeEach
    void setUp() {
        pedidoEntidad = new Pedido();
        pedidoEntidad.setId(1L);
        pedidoEntidad.setClienteId(100L);

        solicitudDTO = new SolicitudCrearPedidoDTO();
        solicitudDTO.setClienteId(100L);
        
        respuestaDTO = new RespuestaPedidoDTO();
        respuestaDTO.setId(1L);
        respuestaDTO.setClienteId(100L);
    }

    @Test
    void cuandoCreaPedido_debeDevolverRespuestaDTO() {
        when(repositorioPedido.save(any(Pedido.class))).thenReturn(pedidoEntidad);
        when(pedidoModelAssembler.toModel(any(Pedido.class))).thenReturn(respuestaDTO);
        
        RespuestaPedidoDTO resultadoDTO = servicioPedido.crearPedido(solicitudDTO);

        assertNotNull(resultadoDTO);
        assertEquals(1L, resultadoDTO.getId());
    }

    @Test
    void cuandoObtienePorIdExistente_debeDevolverRespuestaDTO() {
        when(repositorioPedido.findById(1L)).thenReturn(Optional.of(pedidoEntidad));
        when(pedidoModelAssembler.toModel(pedidoEntidad)).thenReturn(respuestaDTO);

        RespuestaPedidoDTO resultadoDTO = servicioPedido.obtenerPedidoPorId(1L);

        assertNotNull(resultadoDTO);
        assertEquals(1L, resultadoDTO.getId());
    }
}