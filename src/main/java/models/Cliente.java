/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Temporal;

/**
 *
 * @author Samuel
 */
@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "Latitud", columnDefinition = "TEXT")
    private String Latitud; // Ej: "14.6349152,-90.5068821"

    @Column(name = "Longitud", columnDefinition = "TEXT")
    private String Longitud; // Ej: "14.6349152,-90.5068821"

}
