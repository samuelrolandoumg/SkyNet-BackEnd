/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CrearClienteDto;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Roles;
import models.Usuario;
import org.springframework.stereotype.Service;
import projection.ubicacionClienteProjection;
import repository.ClienteRepository;
import repository.RolesRepository;
import repository.UsuarioRepository;
import services.ClienteSvc;

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

    public ClienteSvcImpl(ClienteRepository repository,
            RolesRepository rolesRepo,
            UsuarioRepository usuarioRepo) {
        this.repository = repository;
        this.rolesRepo = rolesRepo;
        this.usuarioRepo = usuarioRepo;
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

    public void crearCliente(CrearClienteDto datos) {
        Cliente nuevo = new Cliente();
        nuevo.setNombreCliente(datos.getNombreCliente());
        nuevo.setNombreNegocio(datos.getNombreNegocio());
        nuevo.setLatitud(datos.getLatitud());
        nuevo.setLongitud(datos.getLongitud());
        nuevo.setNit(datos.getNit());
        nuevo.setTelefono(datos.getTelefono());
        nuevo.setCorreo(datos.getCorreo());
        nuevo.setEstado(datos.getEstado() != null ? datos.getEstado() : true);
        nuevo.setFechaRegistro(new Date());

        Roles rol = rolesRepo.findById(datos.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + datos.getIdRol()));
        nuevo.setRol(rol);

        if (datos.getIdSupervisor() != null) {
            Usuario tecnico = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevo.setTecnico(tecnico);
        }

        repository.save(nuevo);
    }

}
