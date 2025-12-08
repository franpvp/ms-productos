package com.example.ms_productos.application.service;

import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;

public interface InventarioService {

    InventarioDTO crear(CrearInventarioRequest request);

    InventarioDTO obtenerPorProducto(Long idProducto);

    InventarioDTO actualizarCantidad(Long idProducto, Integer nuevaCantidad);

    void eliminar(Long idInventario);
}
