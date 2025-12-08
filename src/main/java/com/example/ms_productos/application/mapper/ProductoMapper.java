package com.example.ms_productos.application.mapper;

import com.example.ms_productos.application.dto.ActualizarProductoRequest;
import com.example.ms_productos.application.dto.CrearProductoRequest;
import com.example.ms_productos.application.dto.ProductoDTO;
import com.example.ms_productos.application.entity.CategoriaEntity;
import com.example.ms_productos.application.entity.ProductoEntity;

public class ProductoMapper {

    private ProductoMapper() {
        // utility
    }

    // ==========================
    // Request → Entity (crear)
    // ==========================
    public static ProductoEntity toEntity(CrearProductoRequest req, CategoriaEntity categoria) {
        if (req == null) return null;

        ProductoEntity e = new ProductoEntity();
        e.setCategoria(categoria);
        e.setNombre(req.getNombre());
        e.setDescripcion(req.getDescripcion());
        e.setMarca(req.getMarca());
        e.setPrecio(req.getPrecio());
        e.setImagenUrl(req.getImagenUrl());
        return e;
    }

    // ==========================
    // Request → actualizar Entity
    // ==========================
    public static void applyUpdate(ProductoEntity entity,
                                   ActualizarProductoRequest req,
                                   CategoriaEntity nuevaCategoria) {

        if (req.getNombre() != null) entity.setNombre(req.getNombre());
        if (req.getDescripcion() != null) entity.setDescripcion(req.getDescripcion());
        if (req.getMarca() != null) entity.setMarca(req.getMarca());
        if (req.getImagenUrl() != null) entity.setImagenUrl(req.getImagenUrl());

        if (req.getPrecio() != null) {
            entity.setPrecio(req.getPrecio().intValue());
        }

        if (nuevaCategoria != null) {
            entity.setCategoria(nuevaCategoria);
        }
    }

    // ==========================
    // Entity → DTO (sin stock)
    // ==========================
    public static ProductoDTO toDTO(ProductoEntity e) {
        if (e == null) return null;

        Long idCategoria = e.getCategoria() != null ? e.getCategoria().getId() : null;

        return ProductoDTO.builder()
                .id(e.getId())
                .idCategoria(idCategoria)
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .marca(e.getMarca())
                .precio(e.getPrecio())
                .imagenUrl(e.getImagenUrl())
                .build();
    }

    // ==========================
    // Entity + stock → DTO
    // ==========================
    public static ProductoDTO toDTO(ProductoEntity e, Integer stock) {
        if (e == null) return null;

        Long idCategoria = e.getCategoria() != null ? e.getCategoria().getId() : null;

        return ProductoDTO.builder()
                .id(e.getId())
                .idCategoria(idCategoria)
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .marca(e.getMarca())
                .precio(e.getPrecio())
                .imagenUrl(e.getImagenUrl())
                .build();
    }
}