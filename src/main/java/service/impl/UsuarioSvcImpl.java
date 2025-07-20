/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.ActualizarUsuarioDto;
import dtos.CrearUsuarioDto;
import dtos.LoginRequestDto;
import dtos.UsuarioDto;
import exceptions.CustomException;
import exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Roles;
import models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projection.DataUserProjection;
import projection.UsuarioListarProjection;
import projection.usuarioById;
import projection.usuariobyrolProjection;
import repository.RolesRepository;
import repository.UsuarioRepository;
import services.UsuarioSvc;
import util.JwtUtil;

/**
 *
 * @author Samuel
 */
@Slf4j
@Service
public class UsuarioSvcImpl implements UsuarioSvc {

    private final UsuarioRepository usuarioRepo;
    private final RolesRepository rolesRepo;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioSvcImpl(UsuarioRepository usuarioRepo, JwtUtil jwtUtil, RolesRepository rolesRepo) {
        this.usuarioRepo = usuarioRepo;
        this.jwtUtil = jwtUtil;
        this.rolesRepo = rolesRepo;
    }

    @Override
    public void crearUsuario(CrearUsuarioDto datos) {
        LocalDateTime fecha = LocalDateTime.now();
        Date fechaConvertida = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());

        Usuario nuevoUsuario = new Usuario();

        nuevoUsuario.setNombre(datos.getNombre());
        nuevoUsuario.setApellido(datos.getApellido());
        nuevoUsuario.setCorreo(datos.getCorreo());
        nuevoUsuario.setUsuario(datos.getUsuario());
        nuevoUsuario.setDpi(datos.getDPI());
        nuevoUsuario.setNit(datos.getNIT());
        nuevoUsuario.setDireccion(datos.getDireccion());
        nuevoUsuario.setContrasena(datos.getContrasena());

        Roles rol = rolesRepo.findById(datos.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + datos.getIdRol()));
        nuevoUsuario.setRol(rol);

        nuevoUsuario.setEstado(true);
        nuevoUsuario.setFechaCreacion(fechaConvertida);

        if (datos.getIdSupervisor() != null) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevoUsuario.setSupervisor(supervisor);
        } else {
            nuevoUsuario.setSupervisor(null);
        }

        if (datos.getIdRol() == 2 && datos.getIdAdmin() != null) {
            Usuario admin = usuarioRepo.findById(datos.getIdAdmin())
                    .orElseThrow(() -> new RuntimeException("Admin no encontrado con ID: " + datos.getIdAdmin()));
            nuevoUsuario.setAdmin(admin);
        } else {
            nuevoUsuario.setAdmin(null);
        }

        if (datos.getIdRol() == 3 && datos.getPuestoTecnico() != null) {
            nuevoUsuario.setPuestoTecnico(datos.getPuestoTecnico());
        } else {
            nuevoUsuario.setPuestoTecnico(null);
        }

        usuarioRepo.save(nuevoUsuario);
    }

    @Override
    public UsuarioDto login(LoginRequestDto loginDto) {
        DataUserProjection usuario = usuarioRepo.login(loginDto.getCorreo(), loginDto.getContrasena());

        if (usuario == null) {
            throw new CustomException(ErrorEnum.S_DESCONOCIDO);
        }

        String token = jwtUtil.generarToken(usuario.getcorreo());

        UsuarioDto respuesta = new UsuarioDto();
        respuesta.setId(usuario.getid());
        respuesta.setNombre(usuario.getnombre());
        respuesta.setRol(usuario.getrol());
        respuesta.setToken(token);

        return respuesta;
    }

    @Override
    public UsuarioDto obtenerUsuarioDesdeToken(HttpServletRequest request) {
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzUyMDM0NTUwLCJleHAiOjE3NTIxMjA5NTB9.8Wjecp6PT2WxkpUOCz_ztzNLTS5oV6epndgeQnAzUVQ";
        String token = jwtUtil.obtenerTokenDesdeHeader(request);

        if (token == null || !jwtUtil.validarToken(token)) {
            throw new CustomException(ErrorEnum.TOKEN_I);
        }

        String correo = jwtUtil.obtenerUsernameDesdeToken(token);
        Usuario usuario = usuarioRepo.findByCorreo(correo);

        if (usuario == null) {
            throw new CustomException(ErrorEnum.S_DESCONOCIDO);
        }

        UsuarioDto respuesta = new UsuarioDto();
        respuesta.setId(usuario.getId());
        respuesta.setNombre(usuario.getNombre());
        respuesta.setRol(usuario.getRol().getRol().name()); // ← 🔥 aquí está el fix bueno
        return respuesta;
    }

    @Override
    public List<usuariobyrolProjection> usuariobyRol(String rol) {
        return usuarioRepo.usuariobyRol(rol);
    }

    @Override
    public List<usuariobyrolProjection> tecnicobySupervisor(Long idSupervisor) {
        return usuarioRepo.tecnicobySupervisor(idSupervisor);
    }

    @Override
    public List<UsuarioListarProjection> listarSupervisores() {
        return usuarioRepo.listarSupervisores();
    }

    @Override
    public List<UsuarioListarProjection> listarTecnicosPorSupervisor(Long idSupervisor) {
        return usuarioRepo.listarTecnicosPorSupervisor(idSupervisor);
    }

    @Override
    public void actualizarUsuario(ActualizarUsuarioDto datos) {
        Usuario usuario = usuarioRepo.findById(datos.getId())
                .orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));

        // ✅ Validar si el correo ya existe en otro usuario
        if (usuarioRepo.existsByCorreoAndIdNot(datos.getCorreo(), datos.getId())) {
            throw new CustomException(ErrorEnum.CORREO_YA_REGISTRADO);
        }

        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setCorreo(datos.getCorreo());
        usuario.setUsuario(datos.getUsuario());
        usuario.setDireccion(datos.getDireccion());
        usuario.setDpi(datos.getDpi());
        usuario.setNit(datos.getNit());
        usuario.setEstado(datos.getEstado() != null ? datos.getEstado() : usuario.getEstado());

        Roles rol = rolesRepo.findById(datos.getIdRol())
                .orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));
        usuario.setRol(rol);

        if (datos.getIdRol() == 3 && datos.getPuestoTecnico() != null && !datos.getPuestoTecnico().isBlank()) {
            usuario.setPuestoTecnico(datos.getPuestoTecnico());
        } else {
            usuario.setPuestoTecnico(null);
        }

        if (datos.getIdSupervisor() != null && datos.getIdSupervisor() != 0) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new CustomException(ErrorEnum.S_DESCONOCIDO));
            usuario.setSupervisor(supervisor);
        } else {
            usuario.setSupervisor(null);
        }

        usuarioRepo.save(usuario);
    }

    @Override
    public ActualizarUsuarioDto obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorEnum.U_NO_REGISTRADO));

        ActualizarUsuarioDto dto = new ActualizarUsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setUsuario(usuario.getUsuario());
        dto.setDireccion(usuario.getDireccion());
        dto.setDpi(usuario.getDpi());
        dto.setNit(usuario.getNit());
        dto.setIdRol(usuario.getRol().getId());
        String rol = this.usuarioRepo.rolById(usuario.getRol().getId());
        dto.setRol(rol);

        dto.setEstado(usuario.getEstado());

        if (usuario.getSupervisor() != null) {
            dto.setIdSupervisor(usuario.getSupervisor().getId());
        }
        dto.setPuestoTecnico(usuario.getPuestoTecnico());

        return dto;
    }

    @Override
    public usuarioById obtenerDatoUsuario(Long idUsuario) {
        return this.usuarioRepo.obtenerDatoUsuario(idUsuario);
    }

    @Override
    public List<usuarioById> obtenerAdmins() {
        return this.usuarioRepo.obtenerAdmins();
    }
}
