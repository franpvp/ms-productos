package com.example.ms_productos.application.controller;

import com.example.ms_productos.application.dto.ActualizarInventarioRequest;
import com.example.ms_productos.application.dto.CrearInventarioRequest;
import com.example.ms_productos.application.dto.InventarioDTO;
import com.example.ms_productos.application.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<InventarioDTO> crear(@RequestBody CrearInventarioRequest request) {
        InventarioDTO creado = inventarioService.crear(request);
        return ResponseEntity.ok(creado);
    }

    @GetMapping()
    public ResponseEntity<List<InventarioDTO>> listar() {
        List<InventarioDTO> dto = inventarioService.listar();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<InventarioDTO> obtenerPorProducto(@PathVariable Long idProducto) {
        InventarioDTO dto = inventarioService.obtenerPorProducto(idProducto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/producto/{idProducto}")
    public ResponseEntity<InventarioDTO> actualizarCantidad(
            @PathVariable Long idProducto,
            @RequestBody ActualizarInventarioRequest request
    ) {
        InventarioDTO dto = inventarioService.actualizarCantidad(idProducto, request.getCantidad());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{idInventario}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idInventario) {
        inventarioService.eliminar(idInventario);
        return ResponseEntity.noContent().build();
    }
}
