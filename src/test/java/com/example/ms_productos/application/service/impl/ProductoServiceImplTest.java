package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.ActualizarProductoRequest;
import com.example.ms_productos.application.dto.CrearProductoRequest;
import com.example.ms_productos.application.dto.ProductoDTO;
import com.example.ms_productos.application.entity.CategoriaEntity;
import com.example.ms_productos.application.entity.InventarioEntity;
import com.example.ms_productos.application.entity.ProductoEntity;
import com.example.ms_productos.application.repository.CategoriaRepository;
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
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private ProductoServiceImpl productoServiceImpl;

    private CategoriaEntity categoriaEntity;
    private ProductoEntity productoEntity;
    private CrearProductoRequest crearRequest;
    private ActualizarProductoRequest actualizarRequest;

    @BeforeEach
    void setUp() {
        categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(1L);
        categoriaEntity.setNombre("GPU");

        productoEntity = new ProductoEntity();
        productoEntity.setId(10L);
        productoEntity.setNombre("RTX 4080");
        productoEntity.setDescripcion("GPU NVIDIA");
        productoEntity.setMarca("NVIDIA");
        productoEntity.setPrecio(1000000);
        productoEntity.setImagenUrl("http://imagen");
        productoEntity.setCategoria(categoriaEntity);

        crearRequest = CrearProductoRequest.builder()
                .idCategoria(1L)
                .nombre("RTX 4080")
                .descripcion("GPU NVIDIA")
                .marca("NVIDIA")
                .precio(1000000)
                .cantidad(5)
                .imagenUrl("http://imagen")
                .rutaImagen("tarjetas-graficas/rtx4080.png")
                .build();

        actualizarRequest = ActualizarProductoRequest.builder()
                .nombre("RTX 4080 Super")
                .descripcion("GPU NVIDIA Super")
                .marca("NVIDIA")
                .precio(1200000.0)
                .idCategoria(1L)
                .imagenUrl("http://imagen-super")
                .build();
    }

    @Test
    void listarTodosTest() {
        // Arrange
        ProductoEntity p1 = new ProductoEntity();
        p1.setId(1L);
        p1.setNombre("RTX 4070");

        ProductoEntity p2 = new ProductoEntity();
        p2.setId(2L);
        p2.setNombre("RTX 4080");

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));
        when(inventarioRepository.findByProductoId(1L))
                .thenReturn(Optional.of(crearInventarioEntity(1L, 10)));
        when(inventarioRepository.findByProductoId(2L))
                .thenReturn(Optional.of(crearInventarioEntity(2L, 5)));

        // Act
        List<ProductoDTO> lista = productoServiceImpl.listarTodos();

        // Assert
        assertThat(lista).hasSize(2);
        assertThat(lista)
                .extracting(ProductoDTO::getNombre)
                .containsExactlyInAnyOrder("RTX 4070", "RTX 4080");

        verify(inventarioRepository, times(1)).findByProductoId(1L);
        verify(inventarioRepository, times(1)).findByProductoId(2L);
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.of(productoEntity));
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.of(crearInventarioEntity(10L, 5)));

        // Act
        ProductoDTO dto = productoServiceImpl.obtenerPorId(10L);

        // Assert
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getNombre()).isEqualTo("RTX 4080");
        assertThat(dto.getIdCategoria()).isEqualTo(1L);

        verify(productoRepository, times(1)).findById(10L);
        verify(inventarioRepository, times(1)).findByProductoId(10L);
    }

    @Test
    void obtenerPorIdNoEncontradoExceptionTest() {
        // Arrange
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> productoServiceImpl.obtenerPorId(99L));

        verify(productoRepository, times(1)).findById(99L);
        verify(inventarioRepository, never()).findByProductoId(anyLong());
    }

    @Test
    void crearTest() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEntity));
        when(productoRepository.save(any(ProductoEntity.class)))
                .thenAnswer(invocation -> {
                    ProductoEntity e = invocation.getArgument(0);
                    e.setId(10L);
                    return e;
                });

        when(inventarioRepository.save(any(InventarioEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductoDTO dto = productoServiceImpl.crear(crearRequest);

        // Assert
        ArgumentCaptor<ProductoEntity> productoCaptor = ArgumentCaptor.forClass(ProductoEntity.class);
        ArgumentCaptor<InventarioEntity> invCaptor = ArgumentCaptor.forClass(InventarioEntity.class);

        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(productoCaptor.capture());
        verify(inventarioRepository, times(1)).save(invCaptor.capture());

        ProductoEntity guardado = productoCaptor.getValue();
        assertThat(guardado.getNombre()).isEqualTo("RTX 4080");
        assertThat(guardado.getDescripcion()).isEqualTo("GPU NVIDIA");
        assertThat(guardado.getCategoria()).isEqualTo(categoriaEntity);

        InventarioEntity inventarioGuardado = invCaptor.getValue();
        assertThat(inventarioGuardado.getProducto()).isEqualTo(guardado);
        assertThat(inventarioGuardado.getCantidad()).isEqualTo(5);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getNombre()).isEqualTo("RTX 4080");
        assertThat(dto.getIdCategoria()).isEqualTo(1L);
    }

    @Test
    void crearCategoriaNoEncontradaExceptionTest() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> productoServiceImpl.crear(crearRequest));

        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, never()).save(any(ProductoEntity.class));
        verify(inventarioRepository, never()).save(any(InventarioEntity.class));
    }

    @Test
    void actualizarTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.of(productoEntity));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEntity));
        when(productoRepository.save(any(ProductoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.of(crearInventarioEntity(10L, 7)));

        // Act
        ProductoDTO dto = productoServiceImpl.actualizar(10L, actualizarRequest);

        // Assert
        verify(productoRepository, times(1)).findById(10L);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(ProductoEntity.class));
        verify(inventarioRepository, times(1)).findByProductoId(10L);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getNombre()).isEqualTo("RTX 4080 Super");
    }

    @Test
    void actualizarProductoNoEncontradoExceptionTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> productoServiceImpl.actualizar(10L, actualizarRequest));

        verify(productoRepository, times(1)).findById(10L);
        verify(categoriaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(ProductoEntity.class));
    }

    @Test
    void actualizarCategoriaNoEncontradaExceptionTest() {
        // Arrange
        when(productoRepository.findById(10L)).thenReturn(Optional.of(productoEntity));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> productoServiceImpl.actualizar(10L, actualizarRequest));

        verify(productoRepository, times(1)).findById(10L);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, never()).save(any(ProductoEntity.class));
    }

    @Test
    void eliminarConInventarioTest() {
        // Arrange
        InventarioEntity inventario = crearInventarioEntity(10L, 5);
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.of(inventario));

        // Act
        productoServiceImpl.eliminar(10L);

        // Assert
        verify(inventarioRepository, times(1)).findByProductoId(10L);
        verify(inventarioRepository, times(1)).delete(inventario);
        verify(productoRepository, times(1)).deleteById(10L);
    }

    @Test
    void eliminarSinInventarioTest() {
        // Arrange
        when(inventarioRepository.findByProductoId(10L))
                .thenReturn(Optional.empty());

        // Act
        productoServiceImpl.eliminar(10L);

        // Assert
        verify(inventarioRepository, times(1)).findByProductoId(10L);
        verify(inventarioRepository, never()).delete(any(InventarioEntity.class));
        verify(productoRepository, times(1)).deleteById(10L);
    }

    private InventarioEntity crearInventarioEntity(Long idProducto, Integer cantidad) {
        InventarioEntity e = new InventarioEntity();
        ProductoEntity p = new ProductoEntity();
        p.setId(idProducto);
        e.setProducto(p);
        e.setCantidad(cantidad);
        return e;
    }
}
