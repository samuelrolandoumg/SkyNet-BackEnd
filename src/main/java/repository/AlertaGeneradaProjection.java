/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

/**
 *
 * @author Samuel
 */
public interface AlertaGeneradaProjection {
    Long getidVisita();
    Long getidTecnico();
    Long getidSupervisor();
    String gettiempoRetraso();
    String getnombreCliente();
}
