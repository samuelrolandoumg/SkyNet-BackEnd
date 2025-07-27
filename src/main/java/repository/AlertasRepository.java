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
import projection.AlertaGeneradaProjection;
import projection.AlertaVisitaProjection;

/**
 *
 * @author Samuel
 */
public interface AlertasRepository extends JpaRepository<AlertaVisita, Long> {

    @Query(value = "SELECT v.id AS idVisita,\n"
            + "       v.id_tecnico AS idTecnico,\n"
            + "       c.nombre_cliente AS nombreCliente,\n"
            + "		CASE \n"
            + "		    WHEN DATE(v.fecha_visita) < CURRENT_DATE THEN\n"
            + "		        CONCAT(\n"
            + "		            CURRENT_DATE - DATE(v.fecha_visita), ' días'\n"
            + "		        )\n"
            + "		    ELSE NULL\n"
            + "		END AS tiempoRetraso\n"
            + "FROM visitas v\n"
            + "INNER JOIN usuarios u ON v.id_tecnico = u.id\n"
            + "INNER JOIN clientes c ON c.id = v.id_cliente\n"
            + "where v.id = :idvisita", nativeQuery = true)
    public AlertaGeneradaProjection dataAlerta(@Param("idvisita") Long idvisita);

    @Query(value = "select av.fecha_alerta as fechaAlerta,\n"
            + "	av.mensaje\n"
            + "from alertas_visita av\n"
            + "inner join visitas v on v.id = av.id_visita\n"
            + "where v.id_tecnico = :idTecnico and av.leido is false", nativeQuery = true)
    List<AlertaVisitaProjection> obtenerAlertasPorTecnico(@Param("idTecnico") Long idTecnico);

    @Modifying
    @Query(value = "UPDATE alertas_visita av\n"
            + "SET leido = true\n"
            + "FROM visitas v\n"
            + "WHERE av.id_visita = v.id\n"
            + "  AND v.id_tecnico = :idTecnico", nativeQuery = true)
    public void marcarAlertasComoLeidas(@Param("idTecnico") Long idTecnico);

}
