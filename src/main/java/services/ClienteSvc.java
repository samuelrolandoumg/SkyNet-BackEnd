/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.ActualizarClienteDto;
import dtos.ClienteDto;
import dtos.CrearClienteDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import projection.ubicacionClienteProjection;
import projection.usuariobyrolProjection;

/**
 *
 * @author Samuel
 */
public interface ClienteSvc {

    public List<ubicacionClienteProjection> clientesbySuper(Long idSupervisor);

    void crearCliente(CrearClienteDto datos);

    public ubicacionClienteProjection coordenadasCliente(Long idCliente);

    public List<usuariobyrolProjection> clientesbyTecnico(Long idTecnico);

    public void actualizarCliente(ActualizarClienteDto datos);

    List<ClienteDto> listarClientes(HttpServletRequest request);
}
