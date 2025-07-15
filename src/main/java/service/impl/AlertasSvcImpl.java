/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import exceptions.CustomException;
import exceptions.ErrorEnum;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import models.AlertaVisita;
import models.Usuario;
import models.Visita;
import org.springframework.stereotype.Service;
import repository.AlertaGeneradaProjection;
import repository.AlertasRepository;
import repository.UsuarioRepository;
import repository.VisitaRepository;
import services.AlertasSvc;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class AlertasSvcImpl implements AlertasSvc {

    private final AlertasRepository repository;
    private final UsuarioRepository usuarioRepo;
    private final VisitaRepository visitaRepo;

    public AlertasSvcImpl(
            AlertasRepository repository,
            UsuarioRepository usuarioRepo,
            VisitaRepository visitaRepo
            ) {
        this.repository = repository;
        this.usuarioRepo = usuarioRepo;
        this.visitaRepo = visitaRepo;
    }

    @Override
    public void alertarRetrasoTecnico(Long idVisita) {
        AlertaGeneradaProjection data = this.repository.dataAlerta(idVisita);

        // Cargar visita, técnico y supervisor como entidades
        Visita visita = visitaRepo.findById(data.getidVisita()).orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));

        Usuario tecnico = usuarioRepo.findById(data.getidTecnico())
                .orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));

        Usuario supervisor = usuarioRepo.findById(data.getidSupervisor())
                .orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));

        // Armar el mensaje personalizado
        String mensaje = "Se ha detectado un retraso de " + data.gettiempoRetraso()
                + " en la visita del cliente: " + data.getnombreCliente();

        // Crear alerta
        AlertaVisita nuevo = new AlertaVisita();
        nuevo.setVisita(visita);
        nuevo.setTecnico(tecnico);
        nuevo.setMensaje(mensaje);
        nuevo.setLeido(false);
        nuevo.setFechaAlerta(Timestamp.from(Instant.now()));
        repository.save(nuevo);
    }

}
