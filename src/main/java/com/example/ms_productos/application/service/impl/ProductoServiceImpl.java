package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.CrearProductoRequest;
import com.example.ms_productos.application.dto.ActualizarProductoRequest;
import com.example.ms_productos.application.dto.ProductoDTO;
import com.example.ms_productos.application.entity.CategoriaEntity;
import com.example.ms_productos.application.entity.InventarioEntity;
import com.example.ms_productos.application.entity.ProductoEntity;
import com.example.ms_productos.application.mapper.ProductoMapper;
import com.example.ms_productos.application.repository.CategoriaRepository;
import com.example.ms_productos.application.repository.InventarioRepository;
import com.example.ms_productos.application.repository.ProductoRepository;
import com.example.ms_productos.application.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(p -> {
                    Integer stock = inventarioRepository.findByProductoId(p.getId())
                            .map(InventarioEntity::getCantidad)
                            .orElse(0);
                    return ProductoMapper.toDTO(p, stock);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        ProductoEntity entity = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Integer stock = inventarioRepository.findByProductoId(id)
                .map(InventarioEntity::getCantidad)
                .orElse(0);

        return ProductoMapper.toDTO(entity, stock);
    }

    @Override
    @Transactional
    public ProductoDTO crear(CrearProductoRequest request) {

        CategoriaEntity categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Crear producto
        ProductoEntity producto = ProductoMapper.toEntity(request, categoria);
        ProductoEntity guardado = productoRepository.save(producto);

        // Crear inventario inicial
        Integer cantidadInicial = request.getCantidad() != null ? request.getCantidad() : 0;
        InventarioEntity inventario = new InventarioEntity();
        inventario.setProducto(guardado);
        inventario.setCantidad(cantidadInicial);
        inventarioRepository.save(inventario);

        return ProductoMapper.toDTO(guardado, inventario.getCantidad());
    }

    @Override
    @Transactional
    public ProductoDTO actualizar(Long idProducto, ActualizarProductoRequest request) {

        ProductoEntity entity = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CategoriaEntity categoria = null;
        if (request.getIdCategoria() != null) {
            categoria = categoriaRepository.findById(request.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        }

        ProductoMapper.applyUpdate(entity, request, categoria);
        ProductoEntity actualizado = productoRepository.save(entity);

        Integer stock = inventarioRepository.findByProductoId(idProducto)
                .map(InventarioEntity::getCantidad)
                .orElse(0);

        return ProductoMapper.toDTO(actualizado, stock);
    }

    @Override
    @Transactional
    public void eliminar(Long idProducto) {

        inventarioRepository.findByProductoId(idProducto)
                .ifPresent(inventarioRepository::delete);

        productoRepository.deleteById(idProducto);
    }
}