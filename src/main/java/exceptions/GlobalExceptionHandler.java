/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Samuel
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> manejarExcepcionPersonalizada(CustomException ex) {
        ErrorEnum error = ex.getError();

        Map<String, Object> body = new HashMap<>();
        body.put("codigo", error.getCodigo());
        body.put("mensaje", error.getMensaje());

        return ResponseEntity.status(error.getEstadoHttp()).body(body);
    }
}
