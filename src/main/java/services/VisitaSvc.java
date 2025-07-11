/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.CrearVisitaDto;
import dtos.iniciarServicioDto;
import java.util.Date;
import java.util.List;
import projection.VisitasTecnicoProjection;

/**
 *
 * @author Samuel
 */
public interface VisitaSvc {

    public void crearVisita(CrearVisitaDto dto);

    public List<VisitasTecnicoProjection> visitasbyTecnico(Long idTecnico);

    public void iniciarServicio(iniciarServicioDto datos);

    public void estado(Long idVisita);

    public void finalizarServicio(iniciarServicioDto datos);

}
