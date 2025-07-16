/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.DetalleVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.ConsultaVisitaSupervisorProjection;
import projection.DatosCorreoClienteProjection;
import projection.DetalleVisitaReporteProjection;
import projection.ResumenEstadoProjection;
import projection.VisitaPorEstadoProjection;

/**
 *
 * @author Samuel
 */
public interface DetalleVisitaRepository extends JpaRepository<DetalleVisita, Long> {

    @Query(value = "select c.correo , c.nombre_cliente as nombre\n"
            + "from visitas v \n"
            + "inner join clientes c on\n"
            + "c.id = v.id_cliente\n"
            + "where v.id = :idVisita\n"
            + "limit 1", nativeQuery = true)
    public DatosCorreoClienteProjection getCorreoCliente(@Param("idVisita") Long idVisita);

    @Query(value = "SELECT c.nombre_cliente as nombreCliente,\n"
            + "	u.nombre || ' ' || u.apellido as nombreTecnico,\n"
            + "	v.hora_ingreso as fechaInicio,\n"
            + "	v.hora_egreso as fechaFin,\n"
            + "	dv.resultado_visita as resultadoVisita,\n"
            + "	dv.observaciones as observaciones,\n"
            + "	dv.comentario_adicional as comentarioAdicional,\n"
            + "	si.fecha_programada as proximaFechaPorIncidencia\n"
            + "FROM clientes c\n"
            + "inner join usuarios u ON c.id_tecnico = u.id\n"
            + "inner join visitas v ON v.id_cliente = c.id\n"
            + "inner join detalle_visita dv on dv.id_visita = v.id\n"
            + "left join seguimiento_incidencia si on si.id_detalle_visita = dv.id\n"
            + "where v.id = :idVisita", nativeQuery = true)
    public DetalleVisitaReporteProjection getDatosReporte(@Param("idVisita") Long idVisita);

    @Query(value = "select url_foto from visitas v \n"
            + "inner join detalle_visita dv on dv.id_visita = v.id\n"
            + "inner join foto_detalle_visita fdv on fdv.id_detalle_visita = dv.id\n"
            + "where v.id = :idVisita limit 1", nativeQuery = true)
    public String getImagen(@Param("idVisita") Long idVisita);

    @Query(value = "select url_foto from visitas v \n"
            + "inner join detalle_visita dv on dv.id_visita = v.id\n"
            + "inner join foto_detalle_visita fdv on fdv.id_detalle_visita = dv.id\n"
            + "where v.id = :idVisita", nativeQuery = true)
    public List<String> getUrlImagen(@Param("idVisita") Long idVisita);

    @Query(value = "select v.estado, COUNT(v.id) as cantidad from visitas v \n"
            + "where v.id_tecnico = :idTecnico\n"
            + "group by v.estado", nativeQuery = true)
    public List<ResumenEstadoProjection> getresumenPorTecnico(@Param("idTecnico") Long idTecnico);

    @Query(value = "SELECT \n"
            + "    v.id AS idVisita,\n"
            + "    v.estado,\n"
            + "    c.nombre_cliente AS nombreCliente,\n"
            + "    v.fecha_visita AS fechaVisita,\n"
            + "    CASE \n"
            + "        WHEN v.estado IN ('FINALIZADO', 'FINALIZADO CON INCIDENCIA') THEN 'Finalizada'\n"
            + "        WHEN v.fecha_visita < CURRENT_DATE THEN 'Fuera de tiempo'\n"
            + "        ELSE 'A tiempo'\n"
            + "    END AS enTiempo\n"
            + "FROM visitas v\n"
            + "INNER JOIN clientes c ON v.id_cliente = c.id\n"
            + "WHERE v.id_tecnico = :idTecnico AND v.estado = :estado", nativeQuery = true)
    public List<VisitaPorEstadoProjection> getVisitasPorEstadoYTecnico(
            @Param("idTecnico") Long idTecnico,
            @Param("estado") String estado);

    @Query(value = "SELECT\n"
            + "  v.estado,\n"
            + "  v.id AS idVisita,\n"
            + "  v.id_tecnico AS idTecnico,\n"
            + "  v.fecha_visita AS fechaVisita,\n"
            + "  c.id AS idCliente,\n"
            + "  c.nombre_cliente AS nombreCliente,\n"
            + "  u.nombre || ' ' || u.apellido AS nombreTecnico,\n"
            + "  av.leido AS leido,\n"
            + "  CASE \n"
            + "    WHEN v.fecha_visita < CURRENT_DATE THEN 'Fuera de tiempo'\n"
            + "    ELSE 'A tiempo'\n"
            + "  END AS enTiempo,\n"
            + "  CASE \n"
            + "    WHEN v.fecha_visita < CURRENT_DATE THEN \n"
            + "      CONCAT(EXTRACT(DAY FROM CURRENT_TIMESTAMP - v.fecha_visita), ' días, ',\n"
            + "             EXTRACT(HOUR FROM CURRENT_TIMESTAMP - v.fecha_visita), ' horas')\n"
            + "    ELSE NULL\n"
            + "  END AS tiempoRetraso\n"
            + "FROM visitas v\n"
            + "JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "JOIN usuarios sup ON u.id_supervisor = sup.id\n"
            + "JOIN clientes c ON c.id = v.id_cliente\n"
            + "LEFT JOIN alertas_visita av ON av.id_visita = v.id\n"
            + "WHERE u.id_supervisor = :idSupervisor\n"
            + "  AND v.estado NOT IN ('FINALIZADO', 'FINALIZADO CON INCIDENCIA')", nativeQuery = true)
    List<ConsultaVisitaSupervisorProjection> getConsultaVisitasPorSupervisor(@Param("idSupervisor") Long idSupervisor);

}
