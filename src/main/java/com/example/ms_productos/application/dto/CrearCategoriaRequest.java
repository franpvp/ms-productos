package com.example.ms_productos.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrearCategoriaRequest {

    private String nombre;
    private String descripcion;
    private String nombreDirectorio;
}