package com.example.ms_productos.application.service;

import com.example.ms_productos.application.dto.ActualizarCategoriaRequest;
import com.example.ms_productos.application.dto.CategoriaDTO;
import com.example.ms_productos.application.dto.CrearCategoriaRequest;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO crear(CrearCategoriaRequest request);

    CategoriaDTO actualizar(Long idCategoria, ActualizarCategoriaRequest request);

    CategoriaDTO obtenerPorId(Long idCategoria);

    List<CategoriaDTO> listar();

    void eliminar(Long idCategoria);
}
