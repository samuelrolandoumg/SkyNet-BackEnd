/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Samuel
 */
@Data
public class CrearVisitaDto {
    private Long idCliente;
    private Long idTecnico;
    private String tipoVisita;
    private LocalDate fechaVisita;
    private Long usuarioCreo;
}
