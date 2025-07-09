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
public class ClienteConsultaDto {

    private Long idCliente;
    private String nombreCliente;
    private String nombreNegocio;
    private String telefono;
    private String correo;
}
