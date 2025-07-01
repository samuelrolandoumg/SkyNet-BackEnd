/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Samuel
 */
public class EstadoHttp {

    /**
     * <p>
     * Codigo estandar para respuestas de exito.</p>
     */
    public static final int OK = 200;
//______________________________________________________________________________
    /**
     * <p>
     * Codigo para respuestas de exito que crearon un nuevo recurso.</p>
     */
    public static final int CREATED = 201;
//______________________________________________________________________________
    /**
     * <p>
     * Codigo de respuesta cuando el servicio solicitado no puede atender la
     * peticion pero existe otro servicio que si puede.</p>
     */
    public static final int SEE_OTHER = 303;
//______________________________________________________________________________
    /**
     * <p>
     * Codigo para respuestas de error por sintaxis incorrecta.</p>
     */
    public static final int BAD_REQUEST = 400;
//______________________________________________________________________________
    /**
     * <p>
     * Codigo para respuestas de error por fallo en la autenticacion y
     * autorizacion de usuarios.</p>
     */
    public static final int UNAUTHORIZED = 401;
//______________________________________________________________________________
    /**
     * <p>
     * Codigo para respuestas de error por solicitud de recursos que no
     * existen.</p>
     */
    public static final int NOT_FOUND = 404;
}
