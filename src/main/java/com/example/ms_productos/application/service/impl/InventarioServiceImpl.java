package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;
import com.example.ms_productos.application.entity.InventarioEntity;
import com.example.ms_productos.application.entity.ProductoEntity;
import com.example.ms_productos.application.mapper.InventarioMapper;
import com.example.ms_productos.application.repository.InventarioRepository;
import com.example.ms_productos.application.repository.ProductoRepository;
import com.example.ms_productos.application.service.InventarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<InventarioDTO> findAll() {
        List<InventarioEntity> listaInventario = inventarioRepository.findAll();
        return listaInventario.stream()
                .map(InventarioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public InventarioDTO crear(CrearInventarioRequest request) {

        // 1) Validar producto
        ProductoEntity producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2) Request → Entity
        InventarioEntity entity = InventarioMapper.toEntity(request, producto);

        // 3) Guardar
        InventarioEntity guardado = inventarioRepository.save(entity);

        // 4) Entity → DTO
        return InventarioMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public InventarioDTO obtenerPorProducto(Long idProducto) {
        InventarioEntity entity = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto"));

        return InventarioMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public InventarioDTO actualizarCantidad(Long idProducto, Integer nuevaCantidad) {
        InventarioEntity entity = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto"));

        entity.setCantidad(nuevaCantidad);
        InventarioEntity actualizado = inventarioRepository.save(entity);

        return InventarioMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long idInventario) {
        inventarioRepository.deleteById(idInventario);
    }
}
