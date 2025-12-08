package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.ActualizarCategoriaRequest;
import com.example.ms_productos.application.dto.CategoriaDTO;
import com.example.ms_productos.application.dto.CrearCategoriaRequest;
import com.example.ms_productos.application.entity.CategoriaEntity;
import com.example.ms_productos.application.mapper.CategoriaMapper;
import com.example.ms_productos.application.repository.CategoriaRepository;
import com.example.ms_productos.application.service.CategoriaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public CategoriaDTO crear(CrearCategoriaRequest request) {
        CategoriaEntity entity = CategoriaMapper.toEntity(request);
        CategoriaEntity guardado = categoriaRepository.save(entity);
        return CategoriaMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public CategoriaDTO actualizar(Long idCategoria, ActualizarCategoriaRequest request) {
        CategoriaEntity entity = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CategoriaMapper.toEntity(entity, request);

        CategoriaEntity actualizado = categoriaRepository.save(entity);
        return CategoriaMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public CategoriaDTO obtenerPorId(Long idCategoria) {
        CategoriaEntity entity = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return CategoriaMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }
}
