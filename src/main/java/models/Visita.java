/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Samuel
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "visitas")
public class Visita {

    //trae el id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_visita", nullable = false)
    private LocalDate fechaVisita;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @Column(name = "hora_ingreso")
    private LocalDateTime horaIngreso;

    @Column(name = "hora_egreso")
    private LocalDateTime horaEgreso;
    
    @Column(name = "latitud_ingreso", columnDefinition = "TEXT")
    private String latitudIngreso;

    @Column(name = "longitud_ingreso", columnDefinition = "TEXT")
    private String longitudIngreso;

    @Column(name = "latitud_egreso", columnDefinition = "TEXT")
    private String latitudEgreso;

    @Column(name = "longitud_egreso", columnDefinition = "TEXT")
    private String longitudEgreso;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Usuario tecnico;

    @ManyToOne
    @JoinColumn(name = "id_supervisor", nullable = false)
    private Usuario supervisor;

    @Column(name = "reporte", columnDefinition = "TEXT")
    private String reporte;

    @Column(name = "enviado_correo", nullable = false)
    private Boolean enviadoCorreo = false;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "CREADO";
}
