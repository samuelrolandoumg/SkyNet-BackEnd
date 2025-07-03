/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.CrearClienteDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projection.ubicacionClienteProjection;
import services.ClienteSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Cliente", description = "Controlador para gestión de categorías")
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteSvc clienteSrv;

    public ClienteController(ClienteSvc clienteSvc) {
        this.clienteSrv = clienteSvc;
    }

    @GetMapping("/coordenadas")
    @Operation(summary = "obtener coordenadas cliente")
    public ResponseEntity<List<ubicacionClienteProjection>> clientesbySuper(@RequestParam Long idSupervisor) {
        return ResponseEntity.ok(clienteSrv.clientesbySuper(idSupervisor));
    }

    @PostMapping("/crear")
    @Operation(summary = "Crea un nuevo cliente con geolocalización")
    public void crearCliente(@RequestBody CrearClienteDto datos) {
        clienteSrv.crearCliente(datos);
    }


}
