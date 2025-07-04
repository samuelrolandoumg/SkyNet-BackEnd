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
public enum ErrorEnum {
    S_DESCONOCIDO   (1000, EstadoHttp.UNAUTHORIZED, "Credenciales de usuario inválidas."),
    TOKEN_I           (1001, EstadoHttp.UNAUTHORIZED, "Token invalido");

    private final int codigo;
    private final int estadoHttp;
    private final String mensaje;

    ErrorEnum(int codigo, int estadoHttp, String mensaje) {
        this.codigo = codigo;
        this.estadoHttp = estadoHttp;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getEstadoHttp() {
        return estadoHttp;
    }

    public String getMensaje() {
        return mensaje;
    }
}
