/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import models.SeguimientoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Samuel
 */
public interface SeguimientoIncidenciaRepository extends JpaRepository<SeguimientoIncidencia, Long> {
    
}
