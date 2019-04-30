package de.cidaas.jwt.exceptions;

public class JWTCreationException extends RuntimeException {
    public JWTCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

