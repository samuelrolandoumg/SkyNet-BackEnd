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
public interface ConsultaVisitaSupervisorProjection {
    String getestado();
    Long getidVisita();
    Long getidTecnico();
    Date getfechaVisita();
    Long getidCliente();
    String getnombreCliente();
    String getnombreTecnico();
    String getenTiempo();
    String gettiempoRetraso(); 
    Boolean getleido();
}
