/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.ActualizarUsuarioDto;
import dtos.CrearUsuarioDto;
import dtos.LoginRequestDto;
import dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projection.UsuarioListarProjection;
import projection.usuarioById;
import projection.usuariobyrolProjection;
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

    @GetMapping("usuarios-rol")
    @Operation(summary = "se listan usuarios segun su rol")
    public ResponseEntity<List<usuariobyrolProjection>> usuariobyRol(@RequestParam String rol) {
        return ResponseEntity.ok(usuarioService.usuariobyRol(rol));
    }

    @GetMapping("/tecnicos-por-supervisor")
    @Operation(summary = "Lista técnicos asignados a un supervisor")
    public ResponseEntity<List<UsuarioListarProjection>> listarTecnicosPorSupervisor(@RequestParam Long idSupervisor) {
        return ResponseEntity.ok(usuarioService.listarTecnicosPorSupervisor(idSupervisor));
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Actualiza un usuario existente")
    public void actualizarUsuario(@RequestBody ActualizarUsuarioDto datos) {
        usuarioService.actualizarUsuario(datos);
    }

    @GetMapping("/obtener/{id}")
    @Operation(summary = "Obtiene un usuario por su ID")
    public ResponseEntity<ActualizarUsuarioDto> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @GetMapping("/obtener-usuario/{idUsuario}")
    @Operation(summary = "Obtiener el usuario por id")
    public ResponseEntity<usuarioById> obtenerDatoUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioService.obtenerDatoUsuario(idUsuario));
    }

    @GetMapping("/lista-admin")
    @Operation(summary = "Obtener la lista de administradores")
    public ResponseEntity<List<usuarioById>> obtenerAdmins() {
        return ResponseEntity.ok(usuarioService.obtenerAdmins());
    }

    @GetMapping("/usuarios-id")
    @Operation(summary = "se listan usuarios segun su id")
    public ResponseEntity<usuariobyrolProjection> usuariobyid(@RequestParam Long idSupervisor) {
        return ResponseEntity.ok(usuarioService.usuariobyid(idSupervisor));
    }

    @PutMapping("/eliminar")
    @Operation(summary = "Deshabilita un usuario si pasa las validaciones")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@RequestParam Long idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Usuario eliminado correctamente.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/contrasena")
    @Operation(summary = "Obtiene la contraseña actual del usuario por ID")
    public ResponseEntity<String> obtenerContrasena(@RequestParam Long idUsuario) {
        String contrasena = usuarioService.obtenerContrasena(idUsuario);
        return ResponseEntity.ok(contrasena);
    }

    @PutMapping("/actualizar-contrasena")
    @Operation(summary = "Actualiza la contraseña del usuario")
    public ResponseEntity<Map<String, String>> actualizarContrasena(
            @RequestParam Long idUsuario,
            @RequestParam String nuevaContrasena
    ) {
        usuarioService.actualizarContrasena(idUsuario, nuevaContrasena);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Contraseña actualizada correctamente.");
        return ResponseEntity.ok(response);
    }

}
