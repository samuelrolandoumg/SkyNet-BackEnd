/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author Samuel
 */
public interface CorreoSvc {

    public void enviarCorreoConAdjunto(String destinatario, String asunto, String cuerpoHtml, byte[] pdf, String nombreArchivo);
    
    public void enviarCorreoSimple(String destinatario, String asunto, String cuerpoHtml);

}
