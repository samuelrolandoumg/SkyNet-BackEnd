/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CrearVisitaDto;
import exceptions.CustomException;
import exceptions.ErrorEnum;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Cliente;
import models.Usuario;
import models.Visita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projection.VisitasTecnicoProjection;
import repository.ClienteRepository;
import repository.UsuarioRepository;
import repository.VisitaRepository;
import services.VisitaSvc;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class VisitaSvcImpl implements VisitaSvc {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private VisitaRepository visitaRepo;

    @Override
    @Transactional
    public void crearVisita(CrearVisitaDto dto) {
        LocalDateTime fecha = LocalDateTime.now();
        Date fechaConvertida = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());

        Visita nueva = new Visita();

        Cliente cliente = clienteRepo.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario tecnico = usuarioRepo.findById(dto.getIdTecnico())
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        Usuario supervisor = usuarioRepo.findById(dto.getIdSupervisor())
                .orElseThrow(() -> new RuntimeException("Supervisor no encontrado"));

        nueva.setCliente(cliente);
        nueva.setTecnico(tecnico);
        nueva.setSupervisor(supervisor);
        nueva.setFechaVisita(dto.getFechaVisita());
        nueva.setFechaCreacion(fechaConvertida);
        nueva.setEstado("CREADO");

        visitaRepo.save(nueva);
    }

    @Override
    public List<VisitasTecnicoProjection> visitasbyTecnico(Long idTecnico) {
        return this.visitaRepo.visitasbyTecnico(idTecnico);
    }

    @Override
    @Transactional
    public void iniciarServicio(Long idVisita) {

        LocalDateTime fecha = LocalDateTime.now(ZoneId.of("America/Guatemala"));
        log.debug("Fecha actual: {}", fecha);

        Integer empezado = this.visitaRepo.getinicioServicio(idVisita);

        if (empezado == 1) {
            throw new CustomException(ErrorEnum.SERVICIO_REGISTRADO);
        }

        this.visitaRepo.iniciarServicio(fecha, idVisita);
    }

}
