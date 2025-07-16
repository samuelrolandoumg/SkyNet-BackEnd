/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dtos.ActualizarClienteDto;
import dtos.ClienteConsultaDto;
import dtos.ClienteDto;
import dtos.CrearClienteDto;
import dtos.TecnicoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projection.ubicacionClienteProjection;
import projection.usuariobyrolProjection;
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
    @Operation(summary = "obtener lista de coordenadas cliente")
    public ResponseEntity<List<ubicacionClienteProjection>> clientesbySuper(@RequestParam Long idSupervisor) {
        return ResponseEntity.ok(clienteSrv.clientesbySuper(idSupervisor));
    }

    @PostMapping("/crear")
    @Operation(summary = "Crea un nuevo cliente con geolocalización")
    public void crearCliente(@RequestBody CrearClienteDto datos) {
        clienteSrv.crearCliente(datos);
    }

    @GetMapping("/coordenadas-cliente")
    @Operation(summary = "obtener coordenadas cliente")
    public ResponseEntity<ubicacionClienteProjection> coordenadasCliente(@RequestParam Long idCliente) {
        return ResponseEntity.ok(clienteSrv.coordenadasCliente(idCliente));
    }

    @GetMapping("clientes-tecnico")
    @Operation(summary = "se listan usuarios segun su rol")
    public ResponseEntity<List<usuariobyrolProjection>> clientesbyTecnico(@RequestParam Long idTecnico) {
        return ResponseEntity.ok(clienteSrv.clientesbyTecnico(idTecnico));
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Actualiza un cliente existente")
    public void actualizarCliente(@RequestBody ActualizarClienteDto datos) {
        clienteSrv.actualizarCliente(datos);
    }

    @GetMapping("/listar")
    @Operation(summary = "Lista clientes según el rol del usuario autenticado")
    public List<ClienteConsultaDto> listarClientes(HttpServletRequest request) {
        return clienteSrv.listarClientes(request);
    }

    @GetMapping("/obtener/{idCliente}")
    @Operation(summary = "Obtiene un cliente por su ID")
    public ClienteDto obtenerClientePorId(@PathVariable Long idCliente) {
        return clienteSrv.obtenerClientePorId(idCliente);
    }

    @GetMapping("/tecnicos-by-rol")
    @Operation(summary = "Lista técnicos según el rol del usuario autenticado")
    public List<TecnicoDto> obtenerTecnicos(HttpServletRequest request) {
        return clienteSrv.obtenerTecnicosPorRolAutenticado(request);
    }

    @PutMapping("/eliminar-cliente")
    @Operation(summary = "Actualiza un cliente existente")
    public void eliminarCliente(@RequestParam Long idCliente) {
        clienteSrv.eliminarCliente(idCliente);
    }

    @GetMapping("/visitas-cliente")
    @Operation(summary = "antes de eliminar, veificar si el cliente tiene visitas pendientes")
    public ResponseEntity<Integer> obtenerVisitaCliente(@RequestParam Long idCliente) {
        return ResponseEntity.ok(clienteSrv.obtenerVisitaCliente(idCliente));
    }
}
