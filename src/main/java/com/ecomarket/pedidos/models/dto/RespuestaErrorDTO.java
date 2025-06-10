package com.ecomarket.pedidos.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@RequiredArgsConstructor 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaErrorDTO {

    // Momento exacto en que ocurrió el error
    private final LocalDateTime timestamp = LocalDateTime.now();

    // El código de estado HTTP (ej: 404, 400, 500)
    private final int status;

    // El texto del estado HTTP (ej: "Not Found", "Bad Request")
    private final String error;

    // Un mensaje claro y legible para el desarrollador sobre qué pasó
    private final String message;

    // La ruta de la API que fue llamada
    private final String path;

    // Un mapa para detallar errores de validación campo por campo.
    // Solo se llenará para errores de tipo 400 (Bad Request).
    private Map<String, String> validationErrors;

}
