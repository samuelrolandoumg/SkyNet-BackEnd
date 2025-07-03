/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Samuel
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Rol rol;
    public enum Rol {
        ADMIN,
        SUPERVISOR,
        TECNICO,
        CLIENTE
    }
}
