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
import java.util.List;
import projection.usuariobyrolProjection;

/**
 *
 * @author Samuel
 */
public interface UsuarioSvc {

    public void crearUsuario(CrearUsuarioDto datos);

    public UsuarioDto login(LoginRequestDto loginDto);

    public UsuarioDto obtenerUsuarioDesdeToken(HttpServletRequest request);

    public List<usuariobyrolProjection> usuariobyRol(String rol);

    public List<usuariobyrolProjection> tecnicobySupervisor(Long idSupervisor);

    
}
