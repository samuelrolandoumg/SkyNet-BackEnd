/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import projection.AlertaVisitaProjection;

/**
 *
 * @author Samuel
 */
public interface AlertasSvc {

    public void alertarRetrasoTecnico(Long idVisita);

    public List<AlertaVisitaProjection> listarAlertasTecnico(Long idTecnico);
}
