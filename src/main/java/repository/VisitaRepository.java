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
import projection.tecnicosbyRolPrejection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;

/**
 *
 * @author Samuel
 */
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query(value = "SELECT \n"
            + "    c.latitud,\n"
            + "    c.longitud,\n"
            + "    c.nombre_cliente AS nombreCliente,\n"
            + "    c.nombre_negocio AS nombreNegocio,\n"
            + "    c.id AS idCliente,\n"
            + "    v.fecha_visita AS fechaVisita,\n"
            + "    v.id AS idVisita,\n"
            + "    v.estado AS estado\n"
            + "FROM clientes c\n"
            + "INNER JOIN visitas v ON v.id_cliente = c.id\n"
            + "WHERE v.id_tecnico = :idTecnico\n"
            + "  AND v.hora_egreso IS NULL\n"
            + "ORDER BY v.fecha_visita DESC", nativeQuery = true)
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

    @Query(value = "SELECT id as idUsuario, \n"
            + "	nombre || ' ' || apellido as nombreTecnico\n"
            + "FROM usuarios\n"
            + "WHERE puesto_tecnico = (\n"
            + "    CASE \n"
            + "        WHEN :tipoVisita = 'Mantenimiento Correctivo' THEN 'Soporte Técnico'\n"
            + "        WHEN :tipoVisita = 'Revisión Preventiva' THEN 'Mantenimiento Preventivo'\n"
            + "        WHEN :tipoVisita = 'Instalación de Cableado' THEN 'Instalación de Infraestructura'\n"
            + "        WHEN :tipoVisita = 'Configuración de Software Empresarial' THEN 'Implementación de Sistemas'\n"
            + "        ELSE NULL\n"
            + "END)", nativeQuery = true)
    public List<tecnicosbyRolPrejection> tecnicoTipoVisita(@Param("tipoVisita") String tipoVisita);

}
