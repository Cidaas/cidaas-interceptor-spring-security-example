package de.cidaas.jwt.exceptions;

import de.cidaas.jwt.exceptions.JWTVerificationException;

public class JWTDecodeException extends JWTVerificationException {
    public JWTDecodeException(String message) {
        this(message, null);
    }

    public JWTDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}