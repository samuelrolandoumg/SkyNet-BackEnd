/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CancelarVisitaDto;
import dtos.CrearVisitaDto;
import dtos.PosponerVisitaDto;
import dtos.iniciarServicioDto;
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
import projection.ResumenEstadoProjection;
import projection.SupervisorVisitaResumenProjection;
import projection.TecnicoVisitaResumenProjection;
import projection.VisitaPorClienteProjection;
import projection.VisitasTecnicoProjection;
import projection.tecnicosbyRolPrejection;
import projection.usuariobyrolProjection;
import projection.visitasSuperByAdminProjection;
import projection.visitasTecnicobySuperProjection;
import repository.ClienteRepository;
import repository.UsuarioRepository;
import repository.VisitaRepository;
import services.CorreoSvc;
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

    @Autowired
    private CorreoSvc correoSvc;

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

        nueva.setCliente(cliente);
        nueva.setTecnico(tecnico);
        nueva.setFechaVisita(dto.getFechaVisita());
        nueva.setFechaCreacion(fechaConvertida);
        nueva.setEstado("CREADO");
        nueva.setTipoVisita(dto.getTipoVisita());
        nueva.setUsuarioCreo(dto.getUsuarioCreo());

        visitaRepo.save(nueva);
    }

    @Override
    public List<VisitasTecnicoProjection> visitasbyTecnico(Long idTecnico) {
        return this.visitaRepo.visitasbyTecnico(idTecnico);
    }

    @Override
    @Transactional
    public void iniciarServicio(iniciarServicioDto datos) {

        LocalDateTime fecha = LocalDateTime.now(ZoneId.of("America/Guatemala"));
        log.debug("Fecha actual: {}", fecha);

        Integer empezado = this.visitaRepo.getinicioServicio(datos.getIdVisita());

        if (empezado == 1) {
            throw new CustomException(ErrorEnum.SERVICIO_REGISTRADO);
        }

        this.visitaRepo.iniciarServicio(
                fecha,
                datos.getLatitud(),
                datos.getLongitud(),
                datos.getEstado(),
                datos.getIdVisita()
        );
    }

    @Override
    public void estado(Long idVisita) {
        Integer empezado = this.visitaRepo.getinicioServicio(idVisita);

        if (empezado == 1) {
            throw new CustomException(ErrorEnum.SERVICIO_REGISTRADO);
        }
    }

    @Override
    @Transactional
    public void finalizarServicio(iniciarServicioDto datos) {
        LocalDateTime fecha = LocalDateTime.now(ZoneId.of("America/Guatemala"));
        this.visitaRepo.finalizarServicio(
                fecha,
                datos.getLatitud(),
                datos.getLongitud(),
                datos.getEstado(),
                datos.getIdVisita()
        );
    }

    @Override
    public List<visitasTecnicobySuperProjection> visitasTecnicobySuper(Long idSupervisor) {
        return this.visitaRepo.visitasTecnicobySuper(idSupervisor);
    }

    @Override
    public List<visitasSuperByAdminProjection> visitasSupervisorbyAdmin(Long idAdmin) {
        return this.visitaRepo.visitasSupervisorbyAdmin(idAdmin);
    }

    @Override
    public List<ResumenEstadoProjection> visitasecnicobyID(Long idTecnico) {
        return this.visitaRepo.visitasecnicobyID(idTecnico);
    }

    @Override
    public List<tecnicosbyRolPrejection> tecnicoTipoVisita(String tipoVisita, Long idUsuario) {

        usuariobyrolProjection data = this.clienteRepo.usuariobyrol(idUsuario);

        if (data.getRol().equals("ADMIN")) {
            return this.visitaRepo.tecnicoTipoVisita(tipoVisita);
        } else if (data.getRol().equals("SUPERVISOR")) {
            return this.visitaRepo.tecnicoTipoVisitaSuper(tipoVisita, idUsuario);
        } else {
            throw new RuntimeException("El rol del usuario no está autorizado para esta operación");
        }
        // return this.visitaRepo.tecnicoTipoVisita(tipoVisita);
    }

    @Override
    public List<SupervisorVisitaResumenProjection> resumenVisitasPorAdmin(Long idAdmin) {
        return visitaRepo.obtenerResumenVisitasPorSupervisor(idAdmin);
    }

    @Override
    public List<TecnicoVisitaResumenProjection> resumenTecnicosPorSupervisor(Long idSupervisor) {
        return visitaRepo.obtenerResumenTecnicosPorSupervisor(idSupervisor);
    }

    @Override
    @Transactional
    public void cancelarVisita(CancelarVisitaDto dto) {
        Visita visita = visitaRepo.findById(dto.getIdVisita())
                .orElseThrow(() -> new CustomException(ErrorEnum.V_NO_ENCONTRADA));

        visita.setEstado("CANCELADA");
        visita.setMotivoCancelacion(dto.getMotivoCancelacion());
        visita.setUsuarioCancelo(dto.getUsuarioCancelo());
        visitaRepo.save(visita);

        // Obtener correo del cliente
        String correo = visita.getCliente().getCorreo();
        String nombre = visita.getCliente().getNombreCliente();

        String cuerpoHtml = "<h3>Hola " + nombre + "</h3>"
                + "<p>Tu visita de <strong>" + visita.getTipoVisita() + "</strong> ha sido cancelada.</p>"
                + "<p><strong>Motivo:</strong> " + dto.getMotivoCancelacion() + "</p>"
                + "<p>Esperamos tu comprensión.</p>"
                + "<p>Para más información puedes contactarte a soporte al <strong>2424-2424</strong>.</p>";

        correoSvc.enviarCorreoSimple(
                correo,
                "Tu visita ha sido cancelada",
                cuerpoHtml
        );
    }

    @Override
    @Transactional
    public void posponerVisita(PosponerVisitaDto dto) {
        Visita visita = visitaRepo.findById(dto.getIdVisita())
                .orElseThrow(() -> new CustomException(ErrorEnum.V_NO_ENCONTRADA));

        if (!visita.getEstado().equalsIgnoreCase("CREADO")) {
            throw new CustomException(ErrorEnum.V_NO_POSPONER);
        }

        visita.setFechaVisita(dto.getNuevaFecha());
        visita.setMotivoPosposicion(dto.getMotivoPosposicion());

        visitaRepo.save(visita);

        // Obtener correo del cliente
        String correo = visita.getCliente().getCorreo();
        String nombre = visita.getCliente().getNombreCliente();

        String cuerpoHtml = "<h3>Hola " + nombre + "</h3>"
                + "<p>Tu visita ha sido <strong>pospuesta</strong> para el día <strong>" + dto.getNuevaFecha() + "</strong>.</p>"
                + "<p><strong>Motivo:</strong> " + dto.getMotivoPosposicion() + "</p>"
                + "<p>Esperamos tu comprensión.</p>"
                + "<p>Para más información puedes contactarte a soporte al <strong>2424-2424</strong>.</p>";

        correoSvc.enviarCorreoSimple(
                correo,
                "Tu visita ha sido pospuesta",
                cuerpoHtml
        );
    }

    @Override
    public List<VisitaPorClienteProjection> listarVisitasPorTecnico(Long idTecnico) {
        return visitaRepo.listarVisitasPorTecnico(idTecnico);
    }

}
