package org.paulojr.concrete.exceptions;

public class TokenNotFoundException extends RuntimeException {

    private final String message;

    public TokenNotFoundException() {
        this.message = "Não autorizado";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
