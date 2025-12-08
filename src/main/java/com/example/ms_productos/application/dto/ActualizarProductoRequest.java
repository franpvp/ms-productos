package com.example.ms_productos.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActualizarProductoRequest {

    private String nombre;
    private String descripcion;
    private String marca;
    private Double precio;
    private String imagenUrl;
    private Long idCategoria;
}