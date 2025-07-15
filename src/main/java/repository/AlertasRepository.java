/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.AlertaVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.AlertaVisitaProjection;

/**
 *
 * @author Samuel
 */
public interface AlertasRepository extends JpaRepository<AlertaVisita, Long> {

    @Query(value = "SELECT v.id AS idVisita,\n"
            + "       v.id_tecnico AS idTecnico,\n"
            + "       v.id_supervisor as idSupervisor,\n"
            + "       c.nombre_cliente AS nombreCliente,\n"
            + "              CASE \n"
            + "           WHEN v.fecha_visita < CURRENT_DATE THEN \n"
            + "               CONCAT(EXTRACT(DAY FROM CURRENT_TIMESTAMP - v.fecha_visita), ' días, ',\n"
            + "                      EXTRACT(HOUR FROM CURRENT_TIMESTAMP - v.fecha_visita), ' horas')\n"
            + "           ELSE NULL\n"
            + "       END AS tiempoRetraso\n"
            + "FROM visitas v\n"
            + "INNER JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "INNER JOIN clientes c ON c.id = v.id_cliente\n"
            + "where v.id = :idvisita", nativeQuery = true)
    public AlertaGeneradaProjection dataAlerta(@Param("idvisita") Long idvisita);

    @Query(value = "select av.fecha_alerta as fechaAlerta,\n"
            + "	av.mensaje\n"
            + "from alertas_visita av\n"
            + "where av.id_tecnico = :idTecnico", nativeQuery = true)
    List<AlertaVisitaProjection> obtenerAlertasPorTecnico(@Param("idTecnico") Long idTecnico);

    @Modifying
    @Query(value = "UPDATE alertas_visita\n"
            + "    SET leido = true\n"
            + "    WHERE id_tecnico = :idTecnico", nativeQuery = true)
    public void marcarAlertasComoLeidas(@Param("idTecnico") Long idTecnico);

}
