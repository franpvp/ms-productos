package com.example.ms_productos.application.mapper;

import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;
import com.example.ms_productos.application.entity.InventarioEntity;
import com.example.ms_productos.application.entity.ProductoEntity;

public class InventarioMapper {

    private InventarioMapper() {}

    // Request → Entity (crear inventario)
    public static InventarioEntity toEntity(CrearInventarioRequest req, ProductoEntity producto) {
        if (req == null) return null;

        InventarioEntity e = new InventarioEntity();
        e.setProducto(producto);
        e.setCantidad(req.getCantidad());
        return e;
    }

    // Entity → DTO
    public static InventarioDTO toDTO(InventarioEntity e) {
        if (e == null) return null;

        Long idProducto = (e.getProducto() != null ? e.getProducto().getId() : null);

        return new InventarioDTO(
                e.getId(),
                idProducto,
                e.getCantidad()
        );
    }
}