/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import models.DetalleVisita;
import models.FotoDetalleVisita;
import models.SeguimientoIncidencia;
import models.Visita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repository.DetalleVisitaRepository;
import repository.FotoDetalleVisitaRepository;
import repository.SeguimientoIncidenciaRepository;
import repository.VisitaRepository;
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

    @Override
    @Transactional
    public void crearDetalleVisita(Long idVisita, String resultadoVisita, String observaciones, String comentarioAdicional, MultipartFile[] fotos) {
        Visita visita = visitaRepo.findById(idVisita).orElseThrow(() -> new RuntimeException("Visita no encontrada"));

        DetalleVisita detalle = new DetalleVisita();
        detalle.setVisita(visita);
        detalle.setResultadoVisita(resultadoVisita);
        detalle.setObservaciones(observaciones);
        detalle.setComentarioAdicional(comentarioAdicional);

        if (resultadoVisita.toLowerCase().contains("Incidencia")) {
            detalle.setTipoIncidencia("GENERICA");
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
    }
}
