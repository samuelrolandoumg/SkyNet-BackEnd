/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import projection.ConsultaVisitaSupervisorProjection;
import projection.ResumenEstadoProjection;
import projection.VisitaPorEstadoProjection;

/**
 *
 * @author Samuel
 */
public interface DetalleVisitaSvc {

   public void crearDetalleVisita(Long idVisita, String resultadoVisita, String observaciones, String comentarioAdicional, MultipartFile[] fotos);

   public byte[] generarPDFVisita(Long idVisita) throws Exception;
   
   public List<ResumenEstadoProjection> resumenPorTecnico(Long idTecnico);

   public List<VisitaPorEstadoProjection> visitasPorEstadoYTecnico(Long idTecnico, String estado);

   public List<ConsultaVisitaSupervisorProjection> consultaVisitasPorSupervisor(Long idSupervisor);
}
