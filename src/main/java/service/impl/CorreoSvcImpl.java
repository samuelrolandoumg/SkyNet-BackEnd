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
    public void enviarCorreoCliente(String destinatario, String asunto, String cuerpoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);
            helper.setFrom("no-reply@skynet.com");

            mailSender.send(mensaje);
            log.info("Correo enviado exitosamente a {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al enviar correo: {}", e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo", e);
        }
    }

}
