/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import dtos.CrearUsuarioDto;
import dtos.LoginRequestDto;
import dtos.UsuarioDto;
import exceptions.CustomException;
import exceptions.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import models.Roles;
import models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projection.DataUserProjection;
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
        Usuario nuevoUsuario = new Usuario();

        nuevoUsuario.setNombre(datos.getNombre());
        nuevoUsuario.setApellido(datos.getApellido());
        nuevoUsuario.setCorreo(datos.getCorreo());
        nuevoUsuario.setUsuario(datos.getUsuario());
        nuevoUsuario.setDpi(datos.getDPI());
        nuevoUsuario.setNit(datos.getNIT());
        nuevoUsuario.setDireccion(datos.getDireccion());
        nuevoUsuario.setContrasena(datos.getContrasena());

        // ✅ Buscar el rol por ID
        Roles rol = rolesRepo.findById(datos.getIdRol()).orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + datos.getIdRol()));
        nuevoUsuario.setRol(rol);

        nuevoUsuario.setEstado(true);
        nuevoUsuario.setFechaCreacion(new Date());

        if (datos.getIdSupervisor() != null) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevoUsuario.setSupervisor(supervisor);
        } else {
            nuevoUsuario.setSupervisor(null);
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
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzUxNTIyNTY0LCJleHAiOjE3NTE2MDg5NjR9.xOPnbMFY3gdmln8nyujWztl3URtMy434hdIDehtKlf0";
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
        respuesta.setRol(usuario.getRol().toString());
        //respuesta.setToken(token);
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
}
