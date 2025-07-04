package com.ecomarket.pedidos.Controlador;

import com.ecomarket.pedidos.controlador.PedidoControlador;
import com.ecomarket.pedidos.models.dto.RespuestaPedidoDTO;
import com.ecomarket.pedidos.models.dto.SolicitudCrearPedidoDTO;
import com.ecomarket.pedidos.excepcion.ExcepcionPedidoNoEncontrado;
import com.ecomarket.pedidos.servicios.ServicioPedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoControlador.class)
public class PedidoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioPedido servicioPedido;

    @Autowired
    private ObjectMapper objectMapper;

    private RespuestaPedidoDTO respuestaDTO;
    private SolicitudCrearPedidoDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        respuestaDTO = new RespuestaPedidoDTO();
        respuestaDTO.setId(1L);
        respuestaDTO.setClienteId(100L);
        respuestaDTO.setMontoTotal(50000L);

        solicitudDTO = new SolicitudCrearPedidoDTO();
        solicitudDTO.setClienteId(100L);
    }

    @Test
    void cuandoCreaPedido_debeRetornar201Created() throws Exception {
        when(servicioPedido.crearPedido(any(SolicitudCrearPedidoDTO.class))).thenReturn(respuestaDTO);

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clienteId").value(100L));
    }

    @Test
    void cuandoObtienePedidoPorIdExistente_debeRetornar200OK() throws Exception {
        when(servicioPedido.obtenerPedidoPorId(1L)).thenReturn(respuestaDTO);

        mockMvc.perform(get("/api/v1/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.montoTotal").value(50000L));
    }

    @Test
    void cuandoObtienePedidoPorIdNoExistente_debeRetornar404NotFound() throws Exception {
        when(servicioPedido.obtenerPedidoPorId(99L)).thenThrow(new ExcepcionPedidoNoEncontrado("Pedido no encontrado"));

        mockMvc.perform(get("/api/v1/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoEliminaPedido_debeRetornar204NoContent() throws Exception {
        doNothing().when(servicioPedido).eliminarPedido(1L);

        mockMvc.perform(delete("/api/v1/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(servicioPedido, times(1)).eliminarPedido(1L);
    }
}
