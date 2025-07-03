/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import models.Cliente;
import models.Roles;
import models.Roles.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Samuel
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "select * from clientes c\n"
            + "where c.nombre = :nombre", nativeQuery = true)
    public Cliente findCategoriasActivas(@Param("nombre") String nombre);

    @Query(value = "select id from roles where id = :idRol", nativeQuery = true)
    public Long finRol(@Param("idRol") Long idRol);

}
