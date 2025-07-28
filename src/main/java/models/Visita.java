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

import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_visita", nullable = false)
    private LocalDate fechaVisita;

    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @Column(name = "hora_ingreso")
    private Date horaIngreso;

    @Column(name = "hora_egreso")
    private Date horaEgreso;

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

    @Column(name = "reporte", columnDefinition = "TEXT")
    private String reporte;

    @Column(name = "enviado_correo", nullable = false)
    private Boolean enviadoCorreo = false;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "CREADO";

    @Column(name = "tipo_visita", nullable = false, length = 100)
    private String tipoVisita;

    @Column(name = "usuario_creo", nullable = false)
    private Long usuarioCreo;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String motivoCancelacion;

    @Column(name = "usuario_cancelo")
    private Long usuarioCancelo;

    @Column(name = "motivo_posposicion", columnDefinition = "TEXT")
    private String motivoPosposicion;
}
