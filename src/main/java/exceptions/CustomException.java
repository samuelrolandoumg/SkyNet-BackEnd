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
public class CustomException extends RuntimeException{

    private final ErrorEnum error;

    public CustomException(ErrorEnum error) {
        super(error.getMensaje());
        this.error = error;
    }

    public ErrorEnum getError() {
        return error;
    }
}
