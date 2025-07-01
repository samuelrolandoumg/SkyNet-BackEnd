/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import services.ClienteSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Categoría", description = "Controlador para gestión de categorías")
@RestController
@RequestMapping("/categoria")
public class ClienteController {

    private final ClienteSvc clienteSrv;

    public ClienteController(ClienteSvc clienteSvc) {
        this.clienteSrv = clienteSvc;
    }

    @GetMapping("/activas")
    @Operation(summary = "Lista las categorías activas")
    public ResponseEntity<List<Cliente>> obtenerCategoriasActivas() {
        return ResponseEntity.ok(clienteSrv.obtenerActivas());
    }

    @PostMapping("/crear")
    @Operation(summary = "se crea")
    public void ingresarCategoria(@RequestParam(name = "nombre", required = true) String nombre,
            @RequestParam(name = "estado", required = true) Boolean estado,
            @RequestParam(name = "descripcion", required = true) String descripcion,
            @RequestParam(name = "fecha", required = true) String fecha) {
        this.clienteSrv.ingresarCategoria(nombre, estado, descripcion, fecha);
    }


}
