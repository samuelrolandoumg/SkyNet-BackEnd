/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import lombok.Data;
import models.Roles.Rol;

/**
 *
 * @author Samuel
 */
@Data
public class CrearUsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String DPI;
    private String NIT;
    private String Direccion;
    private String correo;
    private String usuario;
    private String contrasena;
    private Long idRol;
    private Long idSupervisor; // Solo el ID del supervisor
    private Long idAdmin; // Solo el ID del supervisor
    private Boolean estado;
    private String fechaCreacion; 
    
}
