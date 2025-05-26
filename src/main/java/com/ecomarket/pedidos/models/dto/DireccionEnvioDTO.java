package com.ecomarket.pedidos.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionEnvioDTO {

    @NotBlank(message = "La calle no puede estar vacía.") 
    private String calle;

    @NotBlank(message = "La ciudad no puede estar vacía.")
    private String ciudad;

    @NotBlank(message = "La región no puede estar vacía.")
    private String region; 

    @NotBlank(message = "El código postal no puede estar vacío.")
    private String codigoPostal;

    @NotBlank(message = "El país no puede estar vacío.")
    private String pais;
}