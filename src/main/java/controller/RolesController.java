/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import models.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.RolesSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Roles", description = "Controlador para gestión de roles")
@RestController
@RequestMapping("/roles")
public class RolesController {

    private final RolesSvc rolesSvc;

    public RolesController(RolesSvc rolesSvc) {
        this.rolesSvc = rolesSvc;
    }

    @GetMapping("/roles")
    @Operation(summary = "obtener la lista de roles")
    public ResponseEntity<List<Roles>> roles() {
        return ResponseEntity.ok(rolesSvc.roles());
    }
}
