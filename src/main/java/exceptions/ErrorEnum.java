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
    S_DESCONOCIDO       (1000, EstadoHttp.UNAUTHORIZED, "Credenciales de usuario inválidas."),
    TOKEN_I             (1001, EstadoHttp.UNAUTHORIZED, "Token invalido"),
    ROL_INVALIDO        (1001, EstadoHttp.UNAUTHORIZED, "Rol no autorizado para listar clientes"),
    
    SERVICIO_REGISTRADO (2001, EstadoHttp.BAD_REQUEST, "Ya se ha registrado un inicio de servicio"),
    NO_REGISTRADO       (2002, EstadoHttp.BAD_REQUEST, "Cliente no registrado"),
    U_NO_REGISTRADO     (2003, EstadoHttp.BAD_REQUEST, "Usuario no registrado"),
    CORREO_YA_REGISTRADO(2004, EstadoHttp.BAD_REQUEST,"El correo ya está registrado por otro usuario."),
    V_NO_POSPONER       (2005, EstadoHttp.BAD_REQUEST,"Visita no se puede posponer en este estado"),
    V_NO_ENCONTRADA     (2005, EstadoHttp.BAD_REQUEST,"Visita no encontrada"),
    S_REGISTROS         (2006, EstadoHttp.BAD_REQUEST,"El supervisor tiene tecnicos asignados"),    
    T_REGISTROS         (2007, EstadoHttp.BAD_REQUEST,"El tecnico tiene visitas pendientes");       

    
    
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
