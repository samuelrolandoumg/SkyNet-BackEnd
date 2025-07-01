/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
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
    public List<Cliente> obtenerActivas() {
        return this.repository.findCategoriasActivas();
    }

    @Override
    public void ingresarCategoria(String nombre, Boolean estado, String descripcion, String fecha) {
        Cliente datos = new Cliente();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date fechaParseada = sdf.parse(fecha);

            datos.setNombreCategoria(nombre);
            datos.setEstado(estado);
            datos.setDescripcion(descripcion);
            datos.setFechaCreacion(fechaParseada);

            this.repository.save(datos);
        } catch (ParseException e) {
            // Podés lanzar una excepción controlada o loggear
            throw new RuntimeException("Formato de fecha inválido: " + fecha, e);
        }
    }

}
