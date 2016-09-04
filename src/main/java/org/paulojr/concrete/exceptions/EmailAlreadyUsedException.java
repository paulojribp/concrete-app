package org.paulojr.concrete.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {

    private String message;

    public EmailAlreadyUsedException() {
        this.message = "E-mail já existente";
    }

    @Override
    public String getMessage() {
        return message;
    }

}
