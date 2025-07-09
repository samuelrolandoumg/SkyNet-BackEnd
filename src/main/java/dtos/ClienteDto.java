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
public class ClienteDto {

    private Long id;
    private String nombreCliente;
    private String nombreNegocio;
    private String telefono;
    private String correo;
    private String nit;
    private String latitud;
    private String longitud;
    private Boolean estado;
    private Long idRol;
    private Long idTecnico;
    private String nombreTecnico;
}
