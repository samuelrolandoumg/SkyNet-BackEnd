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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import models.Roles.Rol;
import org.springframework.data.jpa.repository.Temporal;

/**
 *
 * @author Samuel
 */
@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "Latitud", columnDefinition = "TEXT")
    private String Latitud;

    @Column(name = "Longitud", columnDefinition = "TEXT")
    private String Longitud;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Roles rol;

    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Usuario supervisor;

}
