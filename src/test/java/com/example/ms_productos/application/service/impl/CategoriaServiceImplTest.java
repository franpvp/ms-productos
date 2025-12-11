package com.example.ms_productos.application.service.impl;

import com.example.ms_productos.application.dto.ActualizarCategoriaRequest;
import com.example.ms_productos.application.dto.CategoriaDTO;
import com.example.ms_productos.application.dto.CrearCategoriaRequest;
import com.example.ms_productos.application.entity.CategoriaEntity;
import com.example.ms_productos.application.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaServiceImpl;

    private CrearCategoriaRequest crearRequest;
    private ActualizarCategoriaRequest actualizarRequest;
    private CategoriaEntity categoriaEntity;

    @BeforeEach
    void setUp() {
        crearRequest = CrearCategoriaRequest.builder()
                .nombre("GPU")
                .descripcion("Tarjetas de video")
                .nombreDirectorio("tarjetas-graficas")
                .build();

        actualizarRequest = ActualizarCategoriaRequest.builder()
                .nombre("GPUs High-End")
                .descripcion("Tarjetas de video gama alta")
                .nombreDirectorio("tarjetas-graficas-high-end")
                .build();

        categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(1L);
        categoriaEntity.setNombre("GPU");
        categoriaEntity.setDescripcion("Tarjetas de video");
        categoriaEntity.setNombreDirectorio("tarjetas-graficas");
    }

    @Test
    void crearTest() {
        // Arrange
        when(categoriaRepository.save(any(CategoriaEntity.class)))
                .thenAnswer(invocation -> {
                    CategoriaEntity e = invocation.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        // Act
        CategoriaDTO dto = categoriaServiceImpl.crear(crearRequest);

        // Assert
        ArgumentCaptor<CategoriaEntity> captor = ArgumentCaptor.forClass(CategoriaEntity.class);
        verify(categoriaRepository, times(1)).save(captor.capture());

        CategoriaEntity guardado = captor.getValue();
        assertThat(guardado.getNombre()).isEqualTo("GPU");
        assertThat(guardado.getDescripcion()).isEqualTo("Tarjetas de video");
        assertThat(guardado.getNombreDirectorio()).isEqualTo("tarjetas-graficas");

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("GPU");
        assertThat(dto.getDescripcion()).isEqualTo("Tarjetas de video");
        assertThat(dto.getNombreDirectorio()).isEqualTo("tarjetas-graficas");
    }

    @Test
    void actualizarTest() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEntity));
        when(categoriaRepository.save(any(CategoriaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CategoriaDTO dto = categoriaServiceImpl.actualizar(1L, actualizarRequest);

        // Assert
        ArgumentCaptor<CategoriaEntity> captor = ArgumentCaptor.forClass(CategoriaEntity.class);
        verify(categoriaRepository, times(1)).save(captor.capture());

        CategoriaEntity actualizado = captor.getValue();
        assertThat(actualizado.getNombre()).isEqualTo("GPUs High-End");
        assertThat(actualizado.getDescripcion()).isEqualTo("Tarjetas de video gama alta");
        assertThat(actualizado.getNombreDirectorio())
                .isEqualTo("tarjetas-graficas-high-end");

        assertThat(dto.getNombre()).isEqualTo("GPUs High-End");
    }

    @Test
    void actualizarCategoriaNoEncontradaExceptionTest() {
        // Arrange
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> categoriaServiceImpl.actualizar(99L, actualizarRequest));

        verify(categoriaRepository, times(1)).findById(99L);
        verify(categoriaRepository, never()).save(any(CategoriaEntity.class));
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEntity));

        // Act
        CategoriaDTO dto = categoriaServiceImpl.obtenerPorId(1L);

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("GPU");
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdNoEncontradaExceptionTest() {
        // Arrange
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> categoriaServiceImpl.obtenerPorId(99L));

        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    void listarTest() {
        // Arrange
        CategoriaEntity c1 = new CategoriaEntity();
        c1.setId(1L);
        c1.setNombre("GPU");

        CategoriaEntity c2 = new CategoriaEntity();
        c2.setId(2L);
        c2.setNombre("CPU");

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<CategoriaDTO> lista = categoriaServiceImpl.listar();

        // Assert
        assertThat(lista).hasSize(2);
        assertThat(lista)
                .extracting(CategoriaDTO::getNombre)
                .containsExactlyInAnyOrder("GPU", "CPU");
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 1L;

        // Act
        categoriaServiceImpl.eliminar(id);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(id);
    }
}
