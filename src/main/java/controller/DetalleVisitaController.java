/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.DetalleVisitaDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import services.DetalleVisitaSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "DetalleVisita", description = "Controlador para gestión de categorías")
@RestController
@RequestMapping("/DetalleVisita")
public class DetalleVisitaController {

    private final DetalleVisitaSvc detalleVisitaSvc;

    public DetalleVisitaController(DetalleVisitaSvc detalleVisitaSvc) {
        this.detalleVisitaSvc = detalleVisitaSvc;
    }

    @PostMapping(value = "/crear", consumes = "multipart/form-data")
    public ResponseEntity<String> crearDetalleVisita(
            @RequestParam("idVisita") Long idVisita,
            @RequestParam("resultadoVisita") String resultadoVisita,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            @RequestParam(value = "comentarioAdicional", required = false) String comentarioAdicional,
            @RequestPart("fotos") MultipartFile[] fotos) {

        detalleVisitaSvc.crearDetalleVisita(idVisita, resultadoVisita, observaciones, comentarioAdicional, fotos);
        return ResponseEntity.ok("Detalle de visita creado correctamente");
    }
}
