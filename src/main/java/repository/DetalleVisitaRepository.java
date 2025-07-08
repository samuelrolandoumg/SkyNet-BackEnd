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
import projection.DatosCorreoClienteProjection;
import projection.DetalleVisitaReporteProjection;

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
            + "inner join seguimiento_incidencia si on si.id_detalle_visita = dv.id\n"
            + "where v.id = :idVisita", nativeQuery = true)
    public DetalleVisitaReporteProjection getDatosReporte(@Param("idVisita") Long idVisita);

    @Query(value = "select url_foto from visitas v \n"
            + "inner join detalle_visita dv on dv.id_visita = v.id\n"
            + "inner join foto_detalle_visita fdv on fdv.id_detalle_visita = dv.id\n"
            + "where v.id = :idVisita", nativeQuery = true)
    public List<String> getUrlImagen(@Param("idVisita") Long idVisita);
}
