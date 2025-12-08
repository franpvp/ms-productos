package com.example.ms_productos.application.repository;

import com.example.ms_productos.application.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {

    Optional<InventarioEntity> findByProductoId(Long idProducto);
}
