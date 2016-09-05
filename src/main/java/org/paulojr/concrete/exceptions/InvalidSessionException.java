package org.paulojr.concrete.exceptions;

public class InvalidSessionException extends UnauthorizedException {

    public InvalidSessionException() {
        super("Sessão inválida");
    }

}
