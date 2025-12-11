package com.example.ms_productos.application.controller;

import com.example.ms_productos.application.dto.ActualizarProductoRequest;
import com.example.ms_productos.application.dto.CrearProductoRequest;
import com.example.ms_productos.application.dto.ProductoDTO;
import com.example.ms_productos.application.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    void listarTest() {
        
        ProductoDTO p1 = ProductoDTO.builder().id(1L).nombre("RTX 4070").build();
        ProductoDTO p2 = ProductoDTO.builder().id(2L).nombre("RTX 4080").build();

        when(productoService.listarTodos()).thenReturn(List.of(p1, p2));

        
        List<ProductoDTO> lista = productoController.listar();

        
        assertThat(lista).hasSize(2);
        verify(productoService, times(1)).listarTodos();
    }

    @Test
    void obtenerTest() {
        
        Long id = 10L;
        ProductoDTO dtoService = ProductoDTO.builder()
                .id(id)
                .nombre("RTX 4080")
                .build();

        when(productoService.obtenerPorId(id)).thenReturn(dtoService);

        
        ProductoDTO dto = productoController.obtener(id);

        
        assertThat(dto.getId()).isEqualTo(id);
        verify(productoService, times(1)).obtenerPorId(id);
    }

    @Test
    void crearTest() {
        
        CrearProductoRequest request = CrearProductoRequest.builder()
                .idCategoria(1L)
                .nombre("RTX 4080")
                .build();

        ProductoDTO dtoService = ProductoDTO.builder()
                .id(10L)
                .nombre("RTX 4080")
                .build();

        when(productoService.crear(request)).thenReturn(dtoService);

        
        ResponseEntity<ProductoDTO> response = productoController.crear(request);

        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dtoService);
        verify(productoService, times(1)).crear(request);
    }

    @Test
    void actualizarTest() {
        
        Long id = 10L;
        ActualizarProductoRequest request = ActualizarProductoRequest.builder()
                .nombre("RTX 4080 Super")
                .build();

        ProductoDTO dtoService = ProductoDTO.builder()
                .id(id)
                .nombre("RTX 4080 Super")
                .build();

        when(productoService.actualizar(id, request)).thenReturn(dtoService);

        
        ProductoDTO dto = productoController.actualizar(id, request);

        
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getNombre()).isEqualTo("RTX 4080 Super");
        verify(productoService, times(1)).actualizar(id, request);
    }

    @Test
    void eliminarTest() {
        
        Long id = 10L;

        
        productoController.eliminar(id);

        
        verify(productoService, times(1)).eliminar(id);
    }
}
