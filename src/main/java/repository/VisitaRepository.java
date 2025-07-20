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
import projection.SupervisorVisitaResumenProjection;
import projection.TecnicoVisitaResumenProjection;
import projection.VisitaPorClienteProjection;
import projection.VisitasTecnicoProjection;
import projection.tecnicosbyRolPrejection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;

/**
 *
 * @author Samuel
 */
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query(value = "select c.latitud,\n"
            + "    c.longitud,\n"
            + "    c.nombre_cliente AS nombreCliente,\n"
            + "    c.nombre_negocio AS nombreNegocio,\n"
            + "    c.id AS idCliente,\n"
            + "    v.fecha_visita AS fechaVisita,\n"
            + "    v.id AS idVisita,\n"
            + "    v.estado AS estado,\n"
            + "    v.tipo_visita as tipoVisita\n"
            + "FROM clientes c\n"
            + "INNER JOIN visitas v ON v.id_cliente = c.id\n"
            + "WHERE v.id_tecnico = :idTecnico\n"
            + "  AND v.hora_egreso IS null\n"
            + "  and v.estado != 'CANCELADA'\n"
            + "ORDER BY v.fecha_visita desc", nativeQuery = true)
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

    @Query(value = "SELECT id as idUsuario, \n"
            + "	nombre || ' ' || apellido as nombreTecnico\n"
            + "FROM usuarios u\n"
            + "WHERE puesto_tecnico = (\n"
            + "    CASE \n"
            + "        WHEN :tipoVisita = 'Mantenimiento Correctivo' THEN 'Soporte Técnico'\n"
            + "        WHEN :tipoVisita = 'Revisión Preventiva' THEN 'Mantenimiento Preventivo'\n"
            + "        WHEN :tipoVisita = 'Instalación de Cableado' THEN 'Instalación de Infraestructura'\n"
            + "        WHEN :tipoVisita = 'Configuración de Software Empresarial' THEN 'Implementación de Sistemas'\n"
            + "        ELSE NULL\n"
            + "END)\n"
            + "and u.id_supervisor = :idSupervisor", nativeQuery = true)
    public List<tecnicosbyRolPrejection> tecnicoTipoVisitaSuper(@Param("tipoVisita") String tipoVisita, @Param("idSupervisor") Long idSupervisor);

    @Query(value = "SELECT \n"
            + "    u.id AS idSupervisor,\n"
            + "    u.nombre AS nombreSupervisor,\n"
            + "    COUNT(v.id) AS totalVisitas,\n"
            + "    COUNT(CASE WHEN v.estado = 'CREADO' THEN 1 END) AS creadas,\n"
            + "    COUNT(CASE WHEN v.estado = 'SERVICIO INICIADO' THEN 1 END) AS iniciadas,\n"
            + "    COUNT(CASE WHEN v.estado IN ('FINALIZADO CON EXITO', 'FINALIZADO CON INCIDENCIA') THEN 1 END) AS finalizadas,\n"
            + "    COUNT(CASE WHEN v.estado = 'CANCELADA' THEN 1 END) AS canceladas\n"
            + "FROM usuarios u\n"
            + "JOIN clientes c ON u.id = c.id_supervisor\n"
            + "LEFT JOIN visitas v ON c.id = v.id_cliente\n"
            + "WHERE u.id_admin = :idAdmin\n"
            + "GROUP BY u.id, u.nombre", nativeQuery = true)
    public List<SupervisorVisitaResumenProjection> obtenerResumenVisitasPorSupervisor(@Param("idAdmin") Long idAdmin);

    @Query(value = "SELECT\n"
            + "  u.id AS idTecnico,\n"
            + "  u.nombre AS nombreTecnico,\n"
            + "  s.nombre AS nombreSupervisor,\n"
            + "  COUNT(v.id) AS totalVisitas,\n"
            + "  COUNT(CASE WHEN v.estado = 'CREADO' THEN 1 END) AS creadas,\n"
            + "  COUNT(CASE WHEN v.estado = 'SERVICIO INICIADO' THEN 1 END) AS iniciadas,\n"
            + "  COUNT(CASE WHEN v.estado = 'FINALIZADO CON EXITO' THEN 1 END) AS finalizadasExito,\n"
            + "  COUNT(CASE WHEN v.estado = 'FINALIZADO CON INCIDENCIA' THEN 1 END) AS finalizadasIncidencia,\n"
            + "  COUNT(CASE WHEN v.estado = 'CANCELADA' THEN 1 END) AS cancelada\n"
            + "FROM usuarios u\n"
            + "LEFT JOIN usuarios s ON u.id_supervisor = s.id      \n"
            + "LEFT JOIN visitas v ON u.id = v.id_tecnico\n"
            + "WHERE u.id_supervisor = :idSupervisor\n"
            + "GROUP BY u.id, u.nombre, s.nombre", nativeQuery = true)
    public List<TecnicoVisitaResumenProjection> obtenerResumenTecnicosPorSupervisor(@Param("idSupervisor") Long idSupervisor);

    @Query(value = "SELECT \n"
            + "    c.id AS idCliente,\n"
            + "    c.nombre_cliente AS nombreCliente,\n"
            + "    c.nombre_negocio AS nombreNegocio,\n"
            + "    v.id AS idVisita,\n"
            + "    v.fecha_visita AS fechaVisita,\n"
            + "    v.estado AS estadoVisita,\n"
            + "    v.tipo_visita AS tipoVisita,\n"
            + "    v.hora_egreso as fechaFinalizada\n"
            + "FROM visitas v\n"
            + "JOIN clientes c ON v.id_cliente = c.id\n"
            + "WHERE v.id_tecnico = :idTecnico\n"
            + "ORDER BY c.nombre_cliente, v.fecha_visita DESC", nativeQuery = true)
    public List<VisitaPorClienteProjection> listarVisitasPorTecnico(@Param("idTecnico") Long idTecnico);

}
