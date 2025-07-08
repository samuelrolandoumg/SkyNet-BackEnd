/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Samuel
 */
@Data
public class DetalleVisitaReporteDto {

    private String nombreCliente;
    private String nombreTecnico;
    private Date fechaInicio;
    private Date fechaFin;
    private String resultadoVisita;
    private String observaciones;
    private String comentarioAdicional;
    private String proximaFechaPorIncidencia;
    private List<ImagenesDto> fotos;
}
