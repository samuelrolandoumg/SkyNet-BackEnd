/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projection;

import java.util.Date;

/**
 *
 * @author Samuel
 */
public interface DetalleVisitaReporteProjection {

    String getNombreCliente();
    String getNombreTecnico();
    Date getFechaInicio();
    Date getFechaFin();
    String getResultadoVisita();
    String getObservaciones();
    String getComentarioAdicional();
    String getProximaFechaPorIncidencia();
}
