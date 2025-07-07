/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import models.DetalleVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.DatosCorreoClienteProjection;

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

}
