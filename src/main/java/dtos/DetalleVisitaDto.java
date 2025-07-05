/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Samuel
 */
@Data
public class DetalleVisitaDto {

    private Long idVisita;
    private String resultadoVisita;
    private String observaciones;
    private String comentarioAdicional;
    private List<String> fotosBase64;

}
