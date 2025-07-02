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
import lombok.extern.slf4j.Slf4j;
import models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projection.DataUserProjection;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioSvcImpl(UsuarioRepository usuarioRepo, JwtUtil jwtUtil) {
        this.usuarioRepo = usuarioRepo;
        this.jwtUtil = jwtUtil;
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
        nuevoUsuario.setRol(datos.getRol());
        nuevoUsuario.setEstado(true);
        nuevoUsuario.setFechaCreacion(new Date());

        // Validamos si hay un supervisor asignado (solo si el rol lo requiere)
        if (datos.getIdSupervisor() != null) {
            Usuario supervisor = usuarioRepo.findById(datos.getIdSupervisor())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + datos.getIdSupervisor()));
            nuevoUsuario.setSupervisor(supervisor);
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
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXVyYS5nb256YWxlekBza3luZXQuY29tLmd0IiwiaWF0IjoxNzUxMzgxNTkxLCJleHAiOjE3NTE0Njc5OTF9.VdzImwMXzI4nK8yrF8SPkf6uPA_kLPRd4Pt9-DovyWY";
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
}
