/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CrearClienteDto;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Roles;
import models.Usuario;
import org.springframework.stereotype.Service;
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
    public Cliente obtenerActivas() {
        return this.repository.findCategoriasActivas("sa");
    }

    @Override
    public void crearCliente(CrearClienteDto datos) {
        Cliente nuevo = new Cliente();
        nuevo.setNombre(datos.getNombre());
        nuevo.setLatitud(datos.getLatitud());
        nuevo.setLongitud(datos.getLongitud());

        Long roles = repository.finRol(datos.getIdRol());
        if (roles.equals("")) {
            return;
        }

        // ✅ Buscar el rol por ID
        Roles rol = rolesRepo.findById(datos.getIdRol()).orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + datos.getIdRol()));
        nuevo.setRol(rol);
        if (datos.getIdSupervisor() != null) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevo.setSupervisor(supervisor);
        } else {
            nuevo.setSupervisor(null);
        }

        repository.save(nuevo);
    }

}
