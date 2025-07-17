/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.CrearVisitaDto;
import dtos.iniciarServicioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projection.ResumenEstadoProjection;
import projection.VisitasTecnicoProjection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;
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

    @GetMapping("visitas-tecnico")
    @Operation(summary = "se listan usuarios segun su rol")
    public ResponseEntity<List<VisitasTecnicoProjection>> visitasbyTecnico(@RequestParam Long idTecnico) {
        return ResponseEntity.ok(visitaSvc.visitasbyTecnico(idTecnico));
    }

    @PostMapping("iniciar-servicio")
    @Operation(summary = "el tecnico inicia el servicio")
    public void iniciarServicio(@RequestBody iniciarServicioDto datos) {
        visitaSvc.iniciarServicio(datos);
    }

    @GetMapping("estado")
    @Operation(summary = "consulta de estado")
    public void estado(@RequestParam Long idVisita) {
        visitaSvc.estado(idVisita);
    }

    @PostMapping("finalizar-servicio")
    @Operation(summary = "el tecnico inicia el servicio")
    public void finalizarServicio(@RequestBody iniciarServicioDto datos) {
        visitaSvc.finalizarServicio(datos);
    }

    @GetMapping("count-visitas-tecnico")
    @Operation(summary = "count de visitas por tecnicos by supervisor")
    public ResponseEntity<List<visitasTecnicobySuperProjection>> visitasTecnicobySuper(@RequestParam Long idSupervisor) {
        return ResponseEntity.ok(visitaSvc.visitasTecnicobySuper(idSupervisor));
    }

    @GetMapping("count-visitas-supervisor")
    @Operation(summary = "count de visitas por supervisor by admin")
    public ResponseEntity<List<visitasSuperByAdminProjection>> visitasSupervisorbyAdmin(@RequestParam Long idAdmin) {
        return ResponseEntity.ok(visitaSvc.visitasSupervisorbyAdmin(idAdmin));
    }

    @GetMapping("count-visitas-tecnico")
    @Operation(summary = "count de visitas del tecnico por estados")
    public ResponseEntity<List<ResumenEstadoProjection>> visitasecnicobyID(@RequestParam Long idTecnico) {
        return ResponseEntity.ok(visitaSvc.visitasecnicobyID(idTecnico));
    }
}
