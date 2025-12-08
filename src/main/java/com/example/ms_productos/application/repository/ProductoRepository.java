package com.example.ms_productos.application.repository;

import com.example.ms_productos.application.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    List<ProductoEntity> findByCategoriaId(Long idCategoria);
}
