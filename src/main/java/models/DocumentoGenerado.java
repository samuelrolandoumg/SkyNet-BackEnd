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
import lombok.Data;

/**
 *
 * @author Samuel
 */
@Data
@Entity
@Table(name = "documentos_generados")
public class DocumentoGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_documento", columnDefinition = "TEXT", nullable = false)
    private String urlDocumento;

    @Column(name = "nombre_documento", nullable = false)
    private String nombreDocumento;

    @ManyToOne
    @JoinColumn(name = "id_detalle_visita", nullable = false)
    private DetalleVisita detalleVisita;
}
