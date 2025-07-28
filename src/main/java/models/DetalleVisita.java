/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Samuel
 */
@Getter
@Setter
@Entity
@Table(name = "detalle_visita")
public class DetalleVisita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resultado_visita", columnDefinition = "TEXT", nullable = false)
    private String resultadoVisita;

    @Column(name = "tipo_incidencia", columnDefinition = "TEXT")
    private String tipoIncidencia; // aca puede ser null si no fue con incidencia

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "comentario_adicional", columnDefinition = "TEXT")
    private String comentarioAdicional;

    @OneToOne
    @JoinColumn(name = "id_visita", nullable = false, unique = true)
    private Visita visita;

    @OneToMany(mappedBy = "detalleVisita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoDetalleVisita> fotos;
}
