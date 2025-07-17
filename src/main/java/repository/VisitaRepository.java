/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.ResumenEstadoProjection;
import projection.VisitasTecnicoProjection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;

/**
 *
 * @author Samuel
 */
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query(value = "SELECT c.latitud,\n"
            + "	c.longitud,\n"
            + "	c.nombre_cliente as nombreCliente,\n"
            + "	c.nombre_negocio as nombreNegocio,\n"
            + "	c.id as idCliente,\n"
            + "	v.fecha_visita as fechaVisita,\n"
            + "	v.id as idVisita,\n"
            + "	v.estado as estado\n"
            + "FROM clientes c\n"
            + "INNER JOIN usuarios u ON c.id_tecnico = u.id\n"
            + "INNER JOIN visitas v ON v.id_cliente = c.id\n"
            + "where v.id_tecnico = :idTecnico\n"
            + "	and v.hora_egreso is null\n"
            + "order by v.fecha_visita desc", nativeQuery = true)
    public List<VisitasTecnicoProjection> visitasbyTecnico(@Param("idTecnico") Long idTecnico);

    @Query(value = "select case\n"
            + "   when exists (\n"
            + "       select 1\n"
            + "       from visitas v\n"
            + "       where v.id = :idVisita\n"
            + "         and v.hora_ingreso is not null)\n"
            + "   then 1\n"
            + "   else 0\n"
            + "end as resultado", nativeQuery = true)
    public Integer getinicioServicio(@Param("idVisita") Long idVisita);

    @Transactional
    @Modifying
    @Query(value = "update visitas \n"
            + "set hora_ingreso = :fechaIngreso,\n"
            + "    latitud_ingreso = :latitud,\n"
            + "    longitud_ingreso = :longitud,\n"
            + "    estado = :estado\n"
            + "where id = :idVisita", nativeQuery = true)
    public void iniciarServicio(@Param("fechaIngreso") LocalDateTime fechaIngreso, @Param("latitud") String latitud, @Param("longitud") String longitud, @Param("estado") String estado, @Param("idVisita") Long idVisita);

    @Transactional
    @Modifying
    @Query(value = "update visitas \n"
            + "set hora_egreso = :fechaegreso,\n"
            + "    latitud_egreso = :latitud,\n"
            + "    longitud_egreso = :longitud,\n"
            + "    estado = :estado\n"
            + "where id = :idVisita", nativeQuery = true)
    public void finalizarServicio(@Param("fechaegreso") LocalDateTime fechaegreso, @Param("latitud") String latitud, @Param("longitud") String longitud, @Param("estado") String estado, @Param("idVisita") Long idVisita);

    @Query(value = "SELECT \n"
            + "  u.nombre || ' ' || u.apellido AS nombreTecnico,\n"
            + "  COUNT(v.id) AS cantidad\n"
            + "FROM visitas v\n"
            + "INNER JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "WHERE u.id_supervisor = :idSupervisor\n"
            + "GROUP BY nombreTecnico", nativeQuery = true)
    public List<visitasTecnicobySuperProjection> visitasTecnicobySuper(@Param("idSupervisor") Long idSupervisor);

    @Query(value = "SELECT \n"
            + "    CONCAT(sup.nombre, ' ', sup.apellido) AS nombreSupervisor,\n"
            + "    COUNT(v.id) AS cantidad\n"
            + "FROM visitas v\n"
            + "JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "JOIN usuarios sup ON u.id_supervisor = sup.id\n"
            + "JOIN usuarios admin ON sup.id_admin = admin.id\n"
            + "WHERE admin.id = :idAdmin\n"
            + "GROUP BY sup.nombre, sup.apellido", nativeQuery = true)
    public List<visitasSuperByAdminProjection> visitasSupervisorbyAdmin(@Param("idAdmin") Long idAdmin);

    @Query(value = "SELECT \n"
            + "  v.estado,\n"
            + "  COUNT(v.id) AS cantidad\n"
            + "FROM visitas v\n"
            + "INNER JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "WHERE u.id = :idTecnico\n"
            + "group by v.estado", nativeQuery = true)
    public List<ResumenEstadoProjection> visitasecnicobyID(@Param("idTecnico") Long idTecnico);
}
