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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "dpi", nullable = false, length = 100)
    private String dpi;

    @Column(name = "nit", nullable = false, length = 100)
    private String nit;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "usuario", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Roles rol;

    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Usuario supervisor;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Usuario admin;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "puesto_tecnico", length = 100)
    private String puestoTecnico;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date();
}
