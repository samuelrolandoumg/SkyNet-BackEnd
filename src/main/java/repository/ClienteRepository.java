/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import jakarta.transaction.Transactional;
import java.util.List;
import models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.tecnicosbyRolPrejection;
import projection.ubicacionClienteProjection;
import projection.usuariobyrolProjection;

/**
 *
 * @author Samuel
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByTecnicoIdIn(List<Long> ids);

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
            + "c.id_tecnico = u.id\n"
            + "where c.id = :idCliente and c.estado = true", nativeQuery = true)
    public ubicacionClienteProjection coordenadasCliente(@Param("idCliente") Long idCliente);

    @Query(value = "select id from roles where id = :idRol", nativeQuery = true)
    public Long finRol(@Param("idRol") Long idRol);

    @Query(value = "select c.id as idUsuario,\n"
            + "	c.nombre_cliente as usuario\n"
            + "from clientes c \n"
            + "where c.id_tecnico = :idTecnico", nativeQuery = true)
    public List<usuariobyrolProjection> clientesbyTecnico(@Param("idTecnico") Long idTecnico);

    @Query(value = "SELECT u.id as idUsuario, u.nombre || ' ' || u.apellido as nombreTecnico\n"
            + "FROM usuarios u\n"
            + "JOIN roles r ON u.id_rol = r.id\n"
            + "WHERE r.rol = 'TECNICO'\n"
            + "AND (:rolBusqueda = 'ADMIN'\n"
            + "    OR (:rolBusqueda = 'SUPERVISOR' AND u.id_supervisor = :idUsuarioBusqueda))", nativeQuery = true)
    public List<tecnicosbyRolPrejection> tecnicosbyRol(@Param("rolBusqueda") String rolBusqueda, @Param("idUsuarioBusqueda") Long idUsuarioBusqueda);

    List<Cliente> findByEstadoTrue();

    List<Cliente> findByTecnicoIdInAndEstadoTrue(List<Long> idsTecnicos);

    @Modifying
    @Query("UPDATE Cliente c SET c.estado = false WHERE c.id = :idCliente")
    void eliminarCliente(@Param("idCliente") Long idCliente);

    @Query(value = "select COUNT(id) from visitas v\n"
            + "where v.id_cliente = :idCliente\n"
            + "and v.estado NOT IN ('FINALIZADO', 'FINALIZADO CON INCIDENCIA')", nativeQuery = true)
    public Integer obtenerVisitaCliente(@Param("idCliente") Long idCliente);
}
