/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import com.cloudinary.Cloudinary;
import dtos.DetalleVisitaReporteDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import models.DetalleVisita;
import models.FotoDetalleVisita;
import models.SeguimientoIncidencia;
import models.Visita;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import projection.DatosCorreoClienteProjection;
import projection.DetalleVisitaReporteProjection;
import repository.DetalleVisitaRepository;
import repository.FotoDetalleVisitaRepository;
import repository.SeguimientoIncidenciaRepository;
import repository.VisitaRepository;
import services.CorreoSvc;
import services.DetalleVisitaSvc;

/**
 *
 * @author Samuel
 */
@Service
@Slf4j
public class DetalleVisitaSvcImpl implements DetalleVisitaSvc {

    @Autowired
    private VisitaRepository visitaRepo;

    @Autowired
    private DetalleVisitaRepository detalleRepo;

    @Autowired
    private FotoDetalleVisitaRepository fotoRepo;

    @Autowired
    private SeguimientoIncidenciaRepository seguimientoRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CorreoSvc correoSvc;

    @Override
    @Transactional
    public void crearDetalleVisita(Long idVisita, String resultadoVisita, String observaciones, String comentarioAdicional, MultipartFile[] fotos) {
        Visita visita = visitaRepo.findById(idVisita).orElseThrow(() -> new RuntimeException("Visita no encontrada"));

        DetalleVisita detalle = new DetalleVisita();
        detalle.setVisita(visita);
        detalle.setResultadoVisita(resultadoVisita);
        detalle.setObservaciones(observaciones);
        detalle.setComentarioAdicional(comentarioAdicional);
        if (resultadoVisita.toLowerCase().contains("incidencia")) {
            detalle.setTipoIncidencia("CON INCIDENCIA");
        }
        detalleRepo.save(detalle);

        List<FotoDetalleVisita> listaFotos = new ArrayList<>();
        for (MultipartFile file : fotos) {
            try {
                String url = cloudinary.uploader().upload(file.getBytes(), Map.of(
                        "folder", "visitas",
                        "resource_type", "image"
                )).get("secure_url").toString();

                FotoDetalleVisita foto = new FotoDetalleVisita();
                foto.setUrlFoto(url);
                foto.setDetalleVisita(detalle);
                listaFotos.add(foto);

            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen a Cloudinary", e);
            }
        }
        fotoRepo.saveAll(listaFotos);
        detalle.setFotos(listaFotos);
        detalleRepo.save(detalle);

        if (detalle.getTipoIncidencia() != null) {
            SeguimientoIncidencia seguimiento = new SeguimientoIncidencia();
            seguimiento.setDetalleVisita(detalle);
            seguimiento.setCliente(visita.getCliente());
            seguimiento.setTecnico(visita.getTecnico());
            seguimiento.setSupervisor(visita.getSupervisor());
            seguimiento.setFechaProgramada(LocalDate.now().plusDays(20));
            seguimiento.setMotivo(observaciones != null ? observaciones : "Incidencia registrada");
            seguimientoRepo.save(seguimiento);
        }

        //obtener correo cliente
        DatosCorreoClienteProjection correoCliente = detalleRepo.getCorreoCliente(idVisita);

        //ADJUNTAR EL PDF
        try {
            byte[] pdf = this.generarPDFVisita(idVisita);
            correoSvc.enviarCorreoConAdjunto(
                    correoCliente.getCorreo(),
                    "Reporte de visita finalizada",
                    "<h3>Hola " + correoCliente.getNombre() + "</h3><p>Tu visita ha finalizado:</p>" + resultadoVisita,
                    pdf,
                    "reporte_visita.pdf"
            );
        } catch (Exception ex) {
            log.error("Error al generar o enviar el reporte PDF: {}", ex.getMessage());
            throw new RuntimeException("Error al generar o enviar el reporte PDF", ex);
        }

        //si se envia actualizo en db
        visita.setEnviadoCorreo(Boolean.TRUE);
        visitaRepo.save(visita);
    }

    @Override
    public byte[] generarPDFVisita(Long idVisita) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/Jasper/ReporteCliente.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("No se encontró el archivo ReporteCliente.jrxml en /resources/Jasper");
        }

        JasperReport report = JasperCompileManager.compileReport(inputStream);

        DetalleVisitaReporteProjection datos = this.detalleRepo.getDatosReporte(idVisita);

        List<DetalleVisitaReporteDto> dato = new ArrayList<>();
        DetalleVisitaReporteDto data = new DetalleVisitaReporteDto();

        data.setNombreCliente(datos.getNombreCliente());
        data.setNombreTecnico(datos.getNombreTecnico());
        data.setFechaInicio(datos.getFechaInicio());
        data.setFechaFin(datos.getFechaFin());
        data.setResultadoVisita(datos.getResultadoVisita());
        data.setObservaciones(datos.getObservaciones());
        data.setComentarioAdicional(datos.getComentarioAdicional());
        data.setProximaFechaPorIncidencia(datos.getProximaFechaPorIncidencia());
        dato.add(data);

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(dato));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
