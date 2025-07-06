/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            + "	v.fecha_visita as fechaVisita,\n"
            + "	v.id as idVisita,\n"
            + "	v.estado as estado\n"
            + "from visitas v\n"
            + "inner join clientes c on\n"
            + "c.id_tecnico = v.id_tecnico\n"
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

}
