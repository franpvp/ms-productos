package com.example.ms_productos.application.controller;

import com.example.ms_productos.application.dto.ActualizarCategoriaRequest;
import com.example.ms_productos.application.dto.CategoriaDTO;
import com.example.ms_productos.application.dto.CrearCategoriaRequest;
import com.example.ms_productos.application.service.CategoriaService;
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
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void crearTest() {
        CrearCategoriaRequest request = CrearCategoriaRequest.builder()
                .nombre("GPU")
                .build();

        CategoriaDTO dtoService = CategoriaDTO.builder()
                .id(1L)
                .nombre("GPU")
                .build();

        when(categoriaService.crear(request)).thenReturn(dtoService);
        
        ResponseEntity<CategoriaDTO> response = categoriaController.crear(request);
        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(dtoService);
        verify(categoriaService, times(1)).crear(request);
    }

    @Test
    void actualizarTest() {
        
        Long id = 1L;
        ActualizarCategoriaRequest request = ActualizarCategoriaRequest.builder()
                .nombre("NEW")
                .build();

        CategoriaDTO dtoService = CategoriaDTO.builder()
                .id(id)
                .nombre("NEW")
                .build();

        when(categoriaService.actualizar(id, request)).thenReturn(dtoService);

        
        ResponseEntity<CategoriaDTO> response = categoriaController.actualizar(id, request);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(dtoService);
        verify(categoriaService, times(1)).actualizar(id, request);
    }

    @Test
    void obtenerPorIdTest() {
        
        Long id = 1L;
        CategoriaDTO dtoService = CategoriaDTO.builder()
                .id(id)
                .nombre("GPU")
                .build();

        when(categoriaService.obtenerPorId(id)).thenReturn(dtoService);

        
        ResponseEntity<CategoriaDTO> response = categoriaController.obtenerPorId(id);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(id);
        verify(categoriaService, times(1)).obtenerPorId(id);
    }

    @Test
    void listarTest() {
        
        CategoriaDTO c1 = CategoriaDTO.builder().id(1L).build();
        CategoriaDTO c2 = CategoriaDTO.builder().id(2L).build();

        when(categoriaService.listar()).thenReturn(List.of(c1, c2));

        
        ResponseEntity<List<CategoriaDTO>> response = categoriaController.listar();

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(2);
        verify(categoriaService, times(1)).listar();
    }

    @Test
    void eliminarTest() {
        
        Long id = 1L;

        
        ResponseEntity<Void> response = categoriaController.eliminar(id);

        
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(categoriaService, times(1)).eliminar(id);
    }
}
