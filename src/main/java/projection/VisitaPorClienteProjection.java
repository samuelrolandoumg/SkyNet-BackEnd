/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projection;

import java.time.LocalDate;

/**
 *
 * @author Samuel
 */
public interface VisitaPorClienteProjection {
    Long getIdCliente();
    String getNombreCliente();
    String getNombreNegocio();
    Long getIdVisita();
    String getFechaVisita();
    String getEstadoVisita();
    String getTipoVisita();
    String getfechaFinalizada();
}
