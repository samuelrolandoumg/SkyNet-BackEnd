/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import lombok.Data;
import models.Roles;
import models.Usuario;

/**
 *
 * @author Samuel
 */
@Data
public class CrearClienteDto {
    private String nombreCliente;
    private String nombreNegocio;
    private String latitud;
    private String longitud;
    private Long idRol;
    private Long idTecnico;

    private String nit;
    private String telefono;
    private String correo;
    private Boolean estado;
}
