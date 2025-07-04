/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.CrearVisitaDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.VisitaSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Visita", description = "Controlador para gestión de las visitas")
@RestController
@RequestMapping("/visita")
public class VisitaController {

    private final VisitaSvc visitaSvc;

    public VisitaController(VisitaSvc visitaSvc) {
        this.visitaSvc = visitaSvc;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearVisita(@RequestBody CrearVisitaDto dto) {
        visitaSvc.crearVisita(dto);
        return ResponseEntity.ok("Visita creada exitosamente");
    }
}
