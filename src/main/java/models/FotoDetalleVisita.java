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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Samuel
 */
@Getter
@Setter
@Entity
@Table(name = "foto_detalle_visita")
public class FotoDetalleVisita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_foto", columnDefinition = "TEXT", nullable = false)
    private String urlFoto;

    @ManyToOne
    @JoinColumn(name = "id_detalle_visita", nullable = false)
    private DetalleVisita detalleVisita;
}
