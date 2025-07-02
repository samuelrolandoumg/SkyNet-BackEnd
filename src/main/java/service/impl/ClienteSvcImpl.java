/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CrearClienteDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Usuario;
import org.springframework.stereotype.Service;
import repository.ClienteRepository;
import services.ClienteSvc;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class ClienteSvcImpl implements ClienteSvc {

    private final ClienteRepository repository;

    public ClienteSvcImpl(ClienteRepository repository) {
        this.repository = repository;
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
        repository.save(nuevo);
    }

}
