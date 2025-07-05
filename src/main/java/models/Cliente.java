/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;


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

    @Column(name = "nombre_cliente", length = 100)
    private String nombreCliente;

    @Column(name = "nombre_negocio", length = 100)
    private String nombreNegocio;

    @Column(name = "latitud", columnDefinition = "TEXT")
    private String latitud;

    @Column(name = "longitud", columnDefinition = "TEXT")
    private String longitud;

    @Column(name = "nit", length = 20)
    private String nit;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "estado")
    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Roles rol;

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Usuario tecnico;
}
