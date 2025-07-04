/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.VisitasTecnicoProjection;

/**
 *
 * @author Samuel
 */
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query(value = "select c.latitud,\n"
            + "	c.longitud,\n"
            + "	c.nombre_cliente as nombreCliente,\n"
            + "	c.nombre_negocio as nombreNegocio,\n"
            + "	c.id as idCliente,\n"
            + "	v.fecha_visita as fechaVisita\n"
            + "from visitas v\n"
            + "inner join clientes c on\n"
            + "c.id_tecnico = v.id_tecnico\n"
            + "where v.id_tecnico = :idTecnico\n"
            + "order by v.fecha_visita desc", nativeQuery = true)
    public List<VisitasTecnicoProjection> visitasbyTecnico(@Param("idTecnico") Long idTecnico);
}
