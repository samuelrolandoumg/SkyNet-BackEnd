/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projection.AlertaVisitaProjection;
import services.AlertasSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Alertas", description = "Controlador para gestión de alertas del sistema")
@RestController
@RequestMapping("/Alertas")
public class AlertasController {

    private final AlertasSvc alertaSvc;

    public AlertasController(AlertasSvc alertaSvc) {
        this.alertaSvc = alertaSvc;
    }

    @PostMapping("/alertar-retraso-tecnico")
    @Operation(summary = "Genera una alerta de retraso para un técnico sin response")
    public void alertarRetraso(Long idVisita, HttpServletRequest request) {
        //UsuarioDto supervisor = usuarioService.obtenerUsuarioDesdeToken(request);
        alertaSvc.alertarRetrasoTecnico(idVisita);
    }

    @GetMapping("/alertas-tecnico")
    @Operation(summary = "Obtiene las alertas del técnico autenticado y las marca como leídas")
    public ResponseEntity<List<AlertaVisitaProjection>> alertasTecnico(Long idTecnico) {
        return ResponseEntity.ok(alertaSvc.listarAlertasTecnico(idTecnico));
    }

}
