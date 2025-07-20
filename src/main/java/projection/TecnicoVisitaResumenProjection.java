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
public interface TecnicoVisitaResumenProjection {
    String getnombreSupervisor();
    Long getIdTecnico();
    String getNombreTecnico();
    Long getTotalVisitas();
    Long getCreadas();
    Long getIniciadas();
    Long getFinalizadasExito();
    Long getFinalizadasIncidencia();
    Long getCancelada();

}
