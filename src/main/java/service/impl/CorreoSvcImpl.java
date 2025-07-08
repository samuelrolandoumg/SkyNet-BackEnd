/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import services.CorreoSvc;

/**
 *
 * @author Samuel
 */
@Service
@Slf4j
public class CorreoSvcImpl implements CorreoSvc {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarCorreoConAdjunto(String destinatario, String asunto, String cuerpoHtml, byte[] pdf, String nombreArchivo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);

            // Adjuntar PDF
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdf));

            mailSender.send(mensaje);
            log.info("Correo con PDF enviado a {}", destinatario);
        } catch (Exception e) {
            log.error("Error al enviar correo con adjunto: {}", e.getMessage());
            throw new RuntimeException("Error al enviar el correo con adjunto", e);
        }
    }

}
