package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;
import com.example.ms_productos.application.entity.InventarioEntity;
import com.example.ms_productos.application.entity.ProductoEntity;
import com.example.ms_productos.application.repository.InventarioRepository;
import com.example.ms_productos.application.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioServiceImpl;

    private ProductoEntity productoEntity;
    private InventarioEntity inventarioEntity;
    private CrearInventarioRequest crearRequest;

    @BeforeEach
    void setUp() {
        productoEntity = new ProductoEntity();
        productoEntity.setId(10L);
        productoEntity.setNombre("RTX 4080");

        inventarioEntity = new InventarioEntity();
        inventarioEntity.setId(1L);
        inventarioEntity.setProducto(productoEntity);
        inventarioEntity.setCantidad(5);

        crearRequest = CrearInventarioRequest.builder()
                .idProducto(10L)
                .cantidad(5)
                .build();
    }

    @Test
    void findAllTest() {
        // Arrange
        InventarioEntity i1 = new InventarioEntity();
        i1.setId(1L);
        i1.setCantidad(5);

        InventarioEntity i2 = new InventarioEntity();
        i2.setId(2L);
        i2.setCantidad(10);

        when(inventarioRepository.findAll()).thenReturn(List.of(i1, i2));

        // Act
        List<InventarioDTO> lista = inventarioServiceImpl.listar();

        // Assert
        assertThat(lista).hasSize(2);
        assertThat(lista)
                .extracting(InventarioDTO::getCantidad)
                .containsExactlyInAnyOrder(5, 10);
    }

    @Test
    void crearTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.of(productoEntity));
        when(inventarioRepository.save(any(InventarioEntity.class)))
                .thenAnswer(invocation -> {
                    InventarioEntity e = invocation.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        // Act
        InventarioDTO dto = inventarioServiceImpl.crear(crearRequest);

        // Assert
        ArgumentCaptor<InventarioEntity> captor = ArgumentCaptor.forClass(InventarioEntity.class);
        verify(productoRepository, times(1)).findById(10L);
        verify(inventarioRepository, times(1)).save(captor.capture());

        InventarioEntity guardado = captor.getValue();
        assertThat(guardado.getProducto()).isEqualTo(productoEntity);
        assertThat(guardado.getCantidad()).isEqualTo(5);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getIdProducto()).isEqualTo(10L);
        assertThat(dto.getCantidad()).isEqualTo(5);
    }

    @Test
    void crearProductoNoEncontradoExceptionTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> inventarioServiceImpl.crear(crearRequest));

        verify(productoRepository, times(1)).findById(10L);
        verify(inventarioRepository, never()).save(any(InventarioEntity.class));
    }

    @Test
    void obtenerPorProductoTest() {
        // Arrange
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.of(inventarioEntity));

        // Act
        InventarioDTO dto = inventarioServiceImpl.obtenerPorProducto(10L);

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getIdProducto()).isEqualTo(10L);
        assertThat(dto.getCantidad()).isEqualTo(5);

        verify(inventarioRepository, times(1)).findByProductoId(10L);
    }

    @Test
    void obtenerPorProductoNoEncontradoExceptionTest() {
        // Arrange
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> inventarioServiceImpl.obtenerPorProducto(10L));

        verify(inventarioRepository, times(1)).findByProductoId(10L);
    }

    @Test
    void actualizarCantidadTest() {
        // Arrange
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.of(inventarioEntity));
        when(inventarioRepository.save(any(InventarioEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InventarioDTO dto = inventarioServiceImpl.actualizarCantidad(10L, 20);

        // Assert
        ArgumentCaptor<InventarioEntity> captor = ArgumentCaptor.forClass(InventarioEntity.class);
        verify(inventarioRepository, times(1)).findByProductoId(10L);
        verify(inventarioRepository, times(1)).save(captor.capture());

        InventarioEntity actualizado = captor.getValue();
        assertThat(actualizado.getCantidad()).isEqualTo(20);

        assertThat(dto.getCantidad()).isEqualTo(20);
    }

    @Test
    void actualizarCantidadInventarioNoEncontradoExceptionTest() {
        // Arrange
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> inventarioServiceImpl.actualizarCantidad(10L, 20));

        verify(inventarioRepository, times(1)).findByProductoId(10L);
        verify(inventarioRepository, never()).save(any(InventarioEntity.class));
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long idInventario = 1L;

        // Act
        inventarioServiceImpl.eliminar(idInventario);

        // Assert
        verify(inventarioRepository, times(1)).deleteById(idInventario);
    }
}
