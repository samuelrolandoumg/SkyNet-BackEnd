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
public interface VisitaPorEstadoProjection {

    Long getIdVisita();
    String getEstado();
    String getNombreCliente();
    Date getFechaVisita();
    String getenTiempo();
}
