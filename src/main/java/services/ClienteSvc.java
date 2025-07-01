/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.Date;
import java.util.List;
import models.Cliente;

/**
 *
 * @author Samuel
 */
public interface ClienteSvc{

    public List<Cliente> obtenerActivas();
    
    public void ingresarCategoria(String nombre, Boolean estado, String descripcion, String fecha);

}
