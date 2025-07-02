/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.CrearUsuarioDto;
import dtos.LoginRequestDto;
import dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import models.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projection.DataUserProjection;
import services.UsuarioSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Usuario", description = "Controlador para gestión de los usuarios")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioSvc usuarioService;

    public UsuarioController(UsuarioSvc usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    @Operation(summary = "Crea un nuevo usuario")
    public void crearUsuario(@RequestBody CrearUsuarioDto datos) {
        usuarioService.crearUsuario(datos);
    }

    @PostMapping("/login")
    @Operation(summary = "Login y generación de JWT")
    public ResponseEntity<UsuarioDto> login(@RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok(usuarioService.login(loginDto));
    }

    @GetMapping("/detector")
    @Operation(summary = "Obtiene el usuario autenticado desde el token")
    public ResponseEntity<UsuarioDto> obtenerUsuarioDesdeToken(HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioDesdeToken(request));
    }
}
