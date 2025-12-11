package com.example.ms_productos.application.controller;

import com.example.ms_productos.application.dto.ActualizarInventarioRequest;
import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;
import com.example.ms_productos.application.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    @Test
    void crearTest() {
        
        CrearInventarioRequest request = CrearInventarioRequest.builder()
                .idProducto(10L)
                .cantidad(5)
                .build();

        InventarioDTO dtoService = InventarioDTO.builder()
                .id(1L)
                .idProducto(10L)
                .cantidad(5)
                .build();

        when(inventarioService.crear(request)).thenReturn(dtoService);

        
        ResponseEntity<InventarioDTO> response = inventarioController.crear(request);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(dtoService);
        verify(inventarioService, times(1)).crear(request);
    }

    @Test
    void findAllTest() {
        
        InventarioDTO i1 = InventarioDTO.builder().id(1L).cantidad(5).build();
        InventarioDTO i2 = InventarioDTO.builder().id(2L).cantidad(10).build();

        when(inventarioService.findAll()).thenReturn(List.of(i1, i2));

        
        ResponseEntity<List<InventarioDTO>> response = inventarioController.findAll();

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(2);
        verify(inventarioService, times(1)).findAll();
    }

    @Test
    void obtenerPorProductoTest() {
        
        Long idProducto = 10L;
        InventarioDTO dtoService = InventarioDTO.builder()
                .id(1L)
                .idProducto(idProducto)
                .cantidad(5)
                .build();

        when(inventarioService.obtenerPorProducto(idProducto)).thenReturn(dtoService);

        
        ResponseEntity<InventarioDTO> response = inventarioController.obtenerPorProducto(idProducto);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getIdProducto()).isEqualTo(idProducto);
        verify(inventarioService, times(1)).obtenerPorProducto(idProducto);
    }

    @Test
    void actualizarCantidadTest() {
        
        Long idProducto = 10L;
        ActualizarInventarioRequest request = ActualizarInventarioRequest.builder()
                .cantidad(20)
                .build();

        InventarioDTO dtoService = InventarioDTO.builder()
                .id(1L)
                .idProducto(idProducto)
                .cantidad(20)
                .build();

        when(inventarioService.actualizarCantidad(idProducto, 20))
                .thenReturn(dtoService);

        
        ResponseEntity<InventarioDTO> response = inventarioController.actualizarCantidad(idProducto, request);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getCantidad()).isEqualTo(20);
        verify(inventarioService, times(1)).actualizarCantidad(idProducto, 20);
    }

    @Test
    void eliminarTest() {
        
        Long idInventario = 1L;

        
        ResponseEntity<Void> response = inventarioController.eliminar(idInventario);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(inventarioService, times(1)).eliminar(idInventario);
    }
}

