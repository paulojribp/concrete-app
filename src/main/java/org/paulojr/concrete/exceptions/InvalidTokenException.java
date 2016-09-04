package org.paulojr.concrete.exceptions;

public class InvalidTokenException extends RuntimeException {

    private final String message;

    public InvalidTokenException() {
        this.message = "Não autorizado";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
