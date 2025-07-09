/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.ActualizarClienteDto;
import dtos.ClienteDto;
import dtos.CrearClienteDto;
import dtos.UsuarioDto;
import exceptions.CustomException;
import exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Roles;
import models.Usuario;
import org.springframework.stereotype.Service;
import projection.ubicacionClienteProjection;
import projection.usuariobyrolProjection;
import repository.ClienteRepository;
import repository.RolesRepository;
import repository.UsuarioRepository;
import services.ClienteSvc;
import services.UsuarioSvc;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class ClienteSvcImpl implements ClienteSvc {

    private final ClienteRepository repository;
    private final RolesRepository rolesRepo;
    private final UsuarioRepository usuarioRepo;
    private final UsuarioSvc usuarioService;

    public ClienteSvcImpl(ClienteRepository repository,
            RolesRepository rolesRepo,
            UsuarioRepository usuarioRepo,
            UsuarioSvc usuarioService) {
        this.repository = repository;
        this.rolesRepo = rolesRepo;
        this.usuarioRepo = usuarioRepo;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<ubicacionClienteProjection> clientesbySuper(Long idSupervisor) {
        return this.repository.clientesbySuper(idSupervisor);
    }

    @Override
    public ubicacionClienteProjection coordenadasCliente(Long idCliente) {
        return this.repository.coordenadasCliente(idCliente);
    }

    @Override
    public List<usuariobyrolProjection> clientesbyTecnico(Long idTecnico) {
        return this.repository.clientesbyTecnico(idTecnico);
    }

    @Override
    public void crearCliente(CrearClienteDto datos) {
        LocalDateTime fecha = LocalDateTime.now();

        Date fechaConvertida = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());

        Cliente nuevo = new Cliente();
        nuevo.setNombreCliente(datos.getNombreCliente());
        nuevo.setNombreNegocio(datos.getNombreNegocio());
        nuevo.setLatitud(datos.getLatitud());
        nuevo.setLongitud(datos.getLongitud());
        nuevo.setNit(datos.getNit());
        nuevo.setTelefono(datos.getTelefono());
        nuevo.setCorreo(datos.getCorreo());
        nuevo.setEstado(datos.getEstado() != null ? datos.getEstado() : true);
        nuevo.setFechaRegistro(fechaConvertida);

        Roles rol = rolesRepo.findById(datos.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + datos.getIdRol()));
        nuevo.setRol(rol);

        if (datos.getIdTecnico() != null) {
            Usuario tecnico = usuarioRepo.findById(datos.getIdTecnico())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdTecnico()));
            nuevo.setTecnico(tecnico);
        }

        repository.save(nuevo);
    }

    @Override
    public void actualizarCliente(ActualizarClienteDto datos) {
        Cliente cliente = repository.findById(datos.getId()).orElseThrow(() -> new CustomException(ErrorEnum.S_DESCONOCIDO));

        cliente.setNombreCliente(datos.getNombreCliente());
        cliente.setNombreNegocio(datos.getNombreNegocio());
        cliente.setLatitud(datos.getLatitud());
        cliente.setLongitud(datos.getLongitud());
        cliente.setNit(datos.getNit());
        cliente.setTelefono(datos.getTelefono());
        cliente.setCorreo(datos.getCorreo());
        cliente.setEstado(datos.getEstado() != null ? datos.getEstado() : cliente.getEstado());

        // Actualizar rol
        Roles rol = rolesRepo.findById(datos.getIdRol()).orElseThrow(() -> new CustomException(ErrorEnum.S_DESCONOCIDO));

        cliente.setRol(rol);

        // Actualizar técnico (opcional)
        if (datos.getIdTecnico() != null) {
            Usuario tecnico = usuarioRepo.findById(datos.getIdTecnico()).orElseThrow(() -> new CustomException(ErrorEnum.S_DESCONOCIDO));

            cliente.setTecnico(tecnico);
        } else {
            cliente.setTecnico(null); // Puedes ajustar esto si deseas mantener el anterior
        }

        repository.save(cliente);
    }

    @Override
    public List<ClienteDto> listarClientes(HttpServletRequest request) {
        UsuarioDto usuario = usuarioService.obtenerUsuarioDesdeToken(request);
        String rol = usuario.getRol();

        log.debug("trae" + usuario.getRol());
        List<Cliente> clientes;

        if ("ADMIN".equalsIgnoreCase(rol)) {
            clientes = repository.findAll();

        } else if ("SUPERVISOR".equalsIgnoreCase(rol)) {
            List<Usuario> tecnicos = usuarioRepo.findBySupervisorId(usuario.getId());
            List<Long> idsTecnicos = tecnicos.stream()
                    .map(Usuario::getId)
                    .toList();

            clientes = repository.findByTecnicoIdIn(idsTecnicos);

        } else {
            throw new CustomException(ErrorEnum.ROL_INVALIDO);
        }
        return clientes.stream().map(cliente -> {
            ClienteDto dto = new ClienteDto();
            dto.setId(cliente.getId());
            dto.setNombreCliente(cliente.getNombreCliente());
            dto.setNombreNegocio(cliente.getNombreNegocio());
            dto.setTelefono(cliente.getTelefono());
            dto.setCorreo(cliente.getCorreo());
            return dto;
        }).toList();
    }

    @Override
    public ClienteDto obtenerClientePorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorEnum.NO_REGISTRADO));

        ClienteDto dto = new ClienteDto();
        dto.setId(cliente.getId());
        dto.setNombreCliente(cliente.getNombreCliente());
        dto.setNombreNegocio(cliente.getNombreNegocio());
        dto.setTelefono(cliente.getTelefono());
        dto.setCorreo(cliente.getCorreo());
        dto.setNit(cliente.getNit());
        dto.setLatitud(cliente.getLatitud());
        dto.setLongitud(cliente.getLongitud());
        dto.setEstado(cliente.getEstado());
        dto.setIdRol(cliente.getRol().getId());

        if (cliente.getTecnico() != null) {
            dto.setIdTecnico(cliente.getTecnico().getId());
            dto.setNombreTecnico(cliente.getTecnico().getNombre() + " " + cliente.getTecnico().getApellido());
        }

        return dto;
    }

}
