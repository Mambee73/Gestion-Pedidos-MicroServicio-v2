package com.ecomarket.pedidos.excepcion;

import com.ecomarket.pedidos.models.dto.RespuestaErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExcepcionPedidoNoEncontrado.class)
    public ResponseEntity<RespuestaErrorDTO> handlePedidoNoEncontradoException(
            ExcepcionPedidoNoEncontrado ex, WebRequest request) {
        
        RespuestaErrorDTO error = new RespuestaErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
            
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        RespuestaErrorDTO error = new RespuestaErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Error de validación. Revisa los campos.",
                request.getDescription(false)
        );
        error.setValidationErrors(errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaErrorDTO> handleGlobalException(Exception ex, WebRequest request) {
        RespuestaErrorDTO error = new RespuestaErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ha ocurrido un error inesperado en el servidor.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}