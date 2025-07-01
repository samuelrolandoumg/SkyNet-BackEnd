/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dtos.CrearUsuarioDto;
import dtos.LoginRequestDto;
import dtos.UsuarioDto;
import jakarta.servlet.http.HttpServletRequest;
import models.Usuario.Rol;
import org.springframework.http.ResponseEntity;
import projection.DataUserProjection;

/**
 *
 * @author Samuel
 */
public interface UsuarioSvc {

    public void crearUsuario(CrearUsuarioDto datos);

    public UsuarioDto login(LoginRequestDto loginDto);

    public UsuarioDto obtenerUsuarioDesdeToken(HttpServletRequest request);
}
