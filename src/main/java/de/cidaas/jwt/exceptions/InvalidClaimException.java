package de.cidaas.jwt.exceptions;

import de.cidaas.jwt.exceptions.JWTVerificationException;

public class InvalidClaimException extends JWTVerificationException {
    public InvalidClaimException(String message) {
        super(message);
    }
}
