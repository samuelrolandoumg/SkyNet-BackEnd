/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.ubicacionClienteProjection;

/**
 *
 * @author Samuel
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "select c.latitud,\n"
            + "	c.longitud,\n"
            + "	c.nombre_cliente as nombreCliente,\n"
            + "	c.nombre_negocio as nombreNegocio,\n"
            + "	c.id as idCliente\n"
            + "from clientes c\n"
            + "inner join usuarios u on\n"
            + "c.id_tecnico = u.id\n"
            + "where u.id_supervisor = :idSupervisor and c.estado = true", nativeQuery = true)
    public List<ubicacionClienteProjection> clientesbySuper(@Param("idSupervisor") Long idSupervisor);

    @Query(value = "select c.latitud,\n"
            + "	c.longitud,\n"
            + "	c.nombre_cliente as nombreCliente,\n"
            + "	c.nombre_negocio as nombreNegocio,\n"
            + "	c.id as idCliente\n"
            + "from clientes c\n"
            + "inner join usuarios u on\n"
            + "c.id_supervisor = u.id\n"
            + "where c.id = :idCliente and c.estado = true", nativeQuery = true)
    public ubicacionClienteProjection coordenadasCliente(@Param("idCliente") Long idCliente);

    @Query(value = "select id from roles where id = :idRol", nativeQuery = true)
    public Long finRol(@Param("idRol") Long idRol);

}
