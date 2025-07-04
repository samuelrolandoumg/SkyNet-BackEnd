/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import models.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Samuel
 */
public interface VisitaRepository extends JpaRepository<Visita, Long> {
    
}
