/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projection;

/**
 *
 * @author Samuel
 */
public interface VisitasTecnicoProjection {
    String getLatitud();
    String getLongitud();
    String getNombreCliente();
    String getNombreNegocio();
    Long getIdCliente();
    String getfechaVisita();
    Long getidVisita();
    String getEstado();
    String gettipoVisita();
}
