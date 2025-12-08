package com.example.ms_productos.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearProductoRequest {
    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private String marca;
    private Integer precio;
    private Integer cantidad;
    private String imagenUrl;
    private String rutaImagen;
}
