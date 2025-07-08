/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.CorreoSvc;

/**
 *
 * @author Samuel
 */
@Tag(name = "Correo", description = "Controlador para gestión de correos")
@RestController
@RequestMapping("/Correo")
public class CorreoController {

    private final CorreoSvc correoSvc;

    
    public CorreoController(CorreoSvc correoSvc) {
        this.correoSvc = correoSvc;
    }
    @PostMapping("/enviar-prueba")
    public String enviarCorreoPrueba(@RequestParam String destinatario) {
        String asunto = "Correo de prueba desde SkyNet";
        String cuerpoHtml = """
            <h1>Holi</h1>
            <p>Este es un correo de prueba enviado correctamente a <strong>%s</strong>.</p>
        """.formatted(destinatario);

        correoSvc.enviarCorreoCliente(destinatario, asunto, cuerpoHtml);
        return "✅ Correo enviado (ver consola/logs para confirmación)";
    }
}
