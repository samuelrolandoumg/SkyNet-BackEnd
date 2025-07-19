/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.ActualizarClienteDto;
import dtos.ClienteConsultaDto;
import dtos.ClienteDto;
import dtos.CrearClienteDto;
import dtos.TecnicoDto;
import dtos.UsuarioDto;
import exceptions.CustomException;
import exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Roles;
import models.Usuario;
import org.springframework.stereotype.Service;
import projection.SupervisorProjection;
import projection.tecnicosbyRolPrejection;
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
    public List<usuariobyrolProjection> clientesbyTecnico(Long idUsuario) {
        
//        usuariobyrolProjection data = this.repository.usuariobyrol(idUsuario);
//        
//        if (data.getRol().equals("ADMIN")) {
//            return repository.clientesByAdmin(idUsuario);
//        } else if (data.getRol().equals("SUPERVISOR")) {
//            return repository.clientesBySupervisor(idUsuario);
//        } else {
//            throw new RuntimeException("El rol del usuario no está autorizado para esta operación");
//        }

        return this.repository.clientesBytec(idUsuario);
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

        if (datos.getIdSupervisor() != null) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevo.setSupervisor(supervisor);
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
        if (datos.getIdSupervisor() != null) {
            Usuario tecnico = usuarioRepo.findById(datos.getIdSupervisor()).orElseThrow(() -> new CustomException(ErrorEnum.S_DESCONOCIDO));

            cliente.setSupervisor(tecnico);
        } else {
            cliente.setSupervisor(null); // Puedes ajustar esto si deseas mantener el anterior
        }

        repository.save(cliente);
    }

    @Override
    public List<ClienteConsultaDto> listarClientes(HttpServletRequest request) {
        UsuarioDto usuario = usuarioService.obtenerUsuarioDesdeToken(request);
        String rol = usuario.getRol();

        List<Cliente> clientes;

        if ("ADMIN".equalsIgnoreCase(rol)) {
            clientes = repository.findByEstadoTrue(); // Admin ve todos los activos

        } else if ("SUPERVISOR".equalsIgnoreCase(rol)) {
            // Supervisor solo ve los clientes asignados a él
            clientes = repository.findBySupervisorIdAndEstadoTrue(usuario.getId());

        } else {
            throw new CustomException(ErrorEnum.ROL_INVALIDO);
        }

        return clientes.stream().map(cliente -> {
            ClienteConsultaDto dto = new ClienteConsultaDto();
            dto.setIdCliente(cliente.getId());
            dto.setNombreCliente(cliente.getNombreCliente());
            dto.setNombreNegocio(cliente.getNombreNegocio());
            dto.setTelefono(cliente.getTelefono());
            dto.setCorreo(cliente.getCorreo());
            return dto;
        }).toList();
    }

    @Override
    public ClienteDto obtenerClientePorId(Long idCliente) {
        Cliente cliente = repository.findById(idCliente)
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

        if (cliente.getSupervisor() != null) {
            dto.setIdSupervisor(cliente.getSupervisor().getId());
            dto.setNombreSupervisor(cliente.getSupervisor().getNombre() + " " + cliente.getSupervisor().getApellido());
        }

        return dto;
    }

    @Override
    public List<TecnicoDto> obtenerTecnicosPorRolAutenticado(HttpServletRequest request) {
        UsuarioDto usuario = usuarioService.obtenerUsuarioDesdeToken(request);
        String rol = usuario.getRol();
        Long idUsuario = usuario.getId();

        List<tecnicosbyRolPrejection> data = this.repository.tecnicosbyRol(usuario.getRol(), usuario.getId());
        List<TecnicoDto> lista = new ArrayList<>();

        for (tecnicosbyRolPrejection pSet : data) {
            TecnicoDto dto = new TecnicoDto();
            dto.setIdUsuario(pSet.getidUsuario());
            dto.setNombreTecnico(pSet.getnombreTecnico());
            lista.add(dto);
        }
        return lista;
    }

    @Override
    @Transactional
    public void eliminarCliente(Long idCliente) {
        this.repository.eliminarCliente(idCliente);
    }

    public Integer obtenerVisitaCliente(Long idCliente) {
        return this.repository.obtenerVisitaCliente(idCliente);
    }

    public List<SupervisorProjection> obtenerSupervisores() {
        return this.repository.obtenerSupervisores();
    }
}
