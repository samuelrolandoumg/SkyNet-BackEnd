/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Samuel
 */
public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Query(value = "select * from roles", nativeQuery = true)
    public List<Roles> finRoles();
}
