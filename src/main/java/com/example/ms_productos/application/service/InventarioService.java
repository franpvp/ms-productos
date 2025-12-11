package com.example.ms_productos.application.service;

import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;

import java.util.List;

public interface InventarioService {

    List<InventarioDTO> listar();
    InventarioDTO crear(CrearInventarioRequest request);

    InventarioDTO obtenerPorProducto(Long idProducto);

    InventarioDTO actualizarCantidad(Long idProducto, Integer nuevaCantidad);

    void eliminar(Long idInventario);
}
