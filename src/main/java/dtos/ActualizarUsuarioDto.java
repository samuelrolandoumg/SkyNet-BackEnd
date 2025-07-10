/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import lombok.Data;

/**
 *
 * @author Samuel
 */
@Data
public class ActualizarUsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String usuario;
    private String direccion;
    private String dpi;
    private String nit;
    private Long idRol;
    private String Rol;
    private Long idSupervisor; // puede ser null
    private Boolean estado;
}
