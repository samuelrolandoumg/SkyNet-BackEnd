/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import projection.ConsultaVisitaSupervisorProjection;
import projection.DocumentosGeneradosProjection;
import projection.ResumenEstadoProjection;
import projection.VisitaPorEstadoProjection;
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

    @PostMapping("/reporte")
    public void generarReporteVisita(@RequestParam("idVisita") Long idVisita, HttpServletResponse response) {
        try {
            byte[] pdf = detalleVisitaSvc.generarPDFVisita(idVisita);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_visita.pdf");

            OutputStream out = response.getOutputStream();
            out.write(pdf);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }

    @GetMapping("/resumen-estados")
    @Operation(summary = "Resumen de visitas por estado para un técnico")
    public ResponseEntity<List<ResumenEstadoProjection>> resumenVisitas(@RequestParam Long idTecnico) {
        return ResponseEntity.ok(detalleVisitaSvc.resumenPorTecnico(idTecnico));
    }

    @GetMapping("/visitas-estado-tecnico")
    @Operation(summary = "Lista visitas filtradas por estado y técnico")
    public ResponseEntity<List<VisitaPorEstadoProjection>> visitasPorEstadoYTecnico(
            @RequestParam Long idTecnico,
            @RequestParam String estado) {
        return ResponseEntity.ok(detalleVisitaSvc.visitasPorEstadoYTecnico(idTecnico, estado));
    }

    @GetMapping("/consulta-visitas-supervisor")
    @Operation(summary = "Consulta de visitas en estado pendiente asignadas a técnicos de un supervisor")
    public ResponseEntity<List<ConsultaVisitaSupervisorProjection>> consultaVisitasSupervisor(
            @RequestParam Long idSupervisor) {
        return ResponseEntity.ok(detalleVisitaSvc.consultaVisitasPorSupervisor(idSupervisor));
    }

    @GetMapping("documentos-generados-tecnico")
    @Operation(summary = "lista de archivos")
    public ResponseEntity<List<DocumentosGeneradosProjection>> visitasecnicobyID(@RequestParam Long idTecnico) {
        return ResponseEntity.ok(detalleVisitaSvc.visitasecnicobyID(idTecnico));
    }

}
