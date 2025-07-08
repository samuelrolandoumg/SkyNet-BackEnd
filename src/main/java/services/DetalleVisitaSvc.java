/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.DetalleVisitaReporteDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Samuel
 */
public interface DetalleVisitaSvc {

   public void crearDetalleVisita(Long idVisita, String resultadoVisita, String observaciones, String comentarioAdicional, MultipartFile[] fotos);

    byte[] generarPDFVisita(Long idVisita) throws Exception;
}
