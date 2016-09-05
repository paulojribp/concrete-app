package org.paulojr.concrete.exceptions;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException() {
        super("Não autorizado");
    }

}
