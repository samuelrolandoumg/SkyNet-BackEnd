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

public class iniciarServicioDto {
    private Long idVisita;
    private String latitud;
    private String longitud;
    private String estado;
}
