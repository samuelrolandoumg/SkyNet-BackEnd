/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.CrearClienteDto;
import java.util.List;
import models.Cliente;
import projection.ubicacionClienteProjection;

/**
 *
 * @author Samuel
 */
public interface ClienteSvc {

    public List<ubicacionClienteProjection> clientesbySuper(Long idSupervisor);

    void crearCliente(CrearClienteDto datos);

    public ubicacionClienteProjection coordenadasCliente(Long idCliente);
    
}
