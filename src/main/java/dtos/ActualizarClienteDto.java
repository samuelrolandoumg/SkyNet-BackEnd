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
public class ActualizarClienteDto {
    private Long id;
    private String nombreCliente;
    private String nombreNegocio;
    private String latitud;
    private String longitud;
    private String nit;
    private String telefono;
    private String correo;
    private Boolean estado;
    private Long idRol;
    private Long idSupervisor;
}
