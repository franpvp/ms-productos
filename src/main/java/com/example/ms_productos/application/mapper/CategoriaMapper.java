package com.example.ms_productos.application.mapper;

import com.example.ms_productos.application.dto.ActualizarCategoriaRequest;
import com.example.ms_productos.application.dto.CategoriaDTO;
import com.example.ms_productos.application.dto.CrearCategoriaRequest;
import com.example.ms_productos.application.entity.CategoriaEntity;

public class CategoriaMapper {

    public static CategoriaEntity toEntity(CrearCategoriaRequest req) {
        CategoriaEntity e = new CategoriaEntity();
        e.setNombre(req.getNombre());
        e.setDescripcion(req.getDescripcion());
        e.setNombreDirectorio(req.getNombreDirectorio());
        return e;
    }

    public static CategoriaEntity toEntity(CategoriaEntity entity, ActualizarCategoriaRequest req) {
        if (req.getNombre() != null) entity.setNombre(req.getNombre());
        if (req.getDescripcion() != null) entity.setDescripcion(req.getDescripcion());
        if (req.getNombreDirectorio() != null) entity.setNombreDirectorio(req.getNombreDirectorio());
        return entity;
    }

    public static CategoriaDTO toDTO(CategoriaEntity e) {
        return new CategoriaDTO(
                e.getId(),
                e.getNombre(),
                e.getDescripcion(),
                e.getNombreDirectorio()
        );
    }
}