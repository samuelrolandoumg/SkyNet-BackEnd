/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.CrearVisitaDto;
import dtos.iniciarServicioDto;
import java.util.List;
import projection.ResumenEstadoProjection;
import projection.SupervisorVisitaResumenProjection;
import projection.TecnicoVisitaResumenProjection;
import projection.VisitasTecnicoProjection;
import projection.tecnicosbyRolPrejection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;

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

    public List<visitasTecnicobySuperProjection> visitasTecnicobySuper(Long idSupervisor);

    public List<visitasSuperByAdminProjection> visitasSupervisorbyAdmin(Long idAdmin);

    public List<ResumenEstadoProjection> visitasecnicobyID(Long idTecnico);

    public List<tecnicosbyRolPrejection> tecnicoTipoVisita(String tipoVisita,  Long idUsuario);
    
    public List<SupervisorVisitaResumenProjection> resumenVisitasPorAdmin(Long idAdmin);
    
    public List<TecnicoVisitaResumenProjection> resumenTecnicosPorSupervisor(Long idSupervisor);

}
