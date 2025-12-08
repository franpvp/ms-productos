package com.example.ms_productos.application.service;

import com.example.ms_productos.application.dto.ActualizarProductoRequest;
import com.example.ms_productos.application.dto.CrearProductoRequest;
import com.example.ms_productos.application.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listarTodos();

    ProductoDTO obtenerPorId(Long id);

    ProductoDTO crear(CrearProductoRequest request);

    ProductoDTO actualizar(Long idProducto, ActualizarProductoRequest request);

    void eliminar(Long idProducto);
}
