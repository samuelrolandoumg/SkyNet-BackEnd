/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.DetalleVisitaDto;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.DetalleVisita;
import models.FotoDetalleVisita;
import models.SeguimientoIncidencia;
import models.Visita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DetalleVisitaRepository;
import repository.FotoDetalleVisitaRepository;
import repository.SeguimientoIncidenciaRepository;
import repository.VisitaRepository;
import services.CloudinarySvc;
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
    private CloudinarySvc cloudinarySvc;

    @Override
    @Transactional
    public void crearDetalleVisita(DetalleVisitaDto dto) {
        Visita visita = visitaRepo.findById(dto.getIdVisita())
                .orElseThrow(() -> new RuntimeException("Visita no encontrada"));

        DetalleVisita detalle = new DetalleVisita();
        detalle.setVisita(visita);
        detalle.setResultadoVisita(dto.getResultadoVisita());
        detalle.setObservaciones(dto.getObservaciones());
        detalle.setComentarioAdicional(dto.getComentarioAdicional());

        // Si fue con incidencia, guarda el tipo
        if (dto.getResultadoVisita().toLowerCase().contains("incidencia")) {
            detalle.setTipoIncidencia("GENERICA"); // o podrías mapearla desde otro campo si tenés
        }

        detalleRepo.save(detalle);

        // Subir fotos a Cloudinary y guardar registros
        List<FotoDetalleVisita> fotos = new ArrayList<>();
        for (String base64 : dto.getFotosBase64()) {
            String url = cloudinarySvc.subirImagenBase64(base64, "visitas");
            FotoDetalleVisita foto = new FotoDetalleVisita();
            foto.setUrlFoto(url);
            foto.setDetalleVisita(detalle);
            fotos.add(foto);
        }
        fotoRepo.saveAll(fotos);

        // Si tiene incidencia → crear seguimiento
        if (detalle.getTipoIncidencia() != null) {
            SeguimientoIncidencia seguimiento = new SeguimientoIncidencia();
            seguimiento.setDetalleVisita(detalle);
            seguimiento.setCliente(visita.getCliente());
            seguimiento.setTecnico(visita.getTecnico());
            seguimiento.setSupervisor(visita.getSupervisor());
            seguimiento.setFechaProgramada(LocalDate.now().plusDays(20));
            seguimiento.setMotivo(detalle.getObservaciones() != null
                    ? detalle.getObservaciones()
                    : "Incidencia registrada en visita");

            seguimientoRepo.save(seguimiento);
        }

        log.info("Detalle de visita creado correctamente con ID: {}", detalle.getId());
    }
}
