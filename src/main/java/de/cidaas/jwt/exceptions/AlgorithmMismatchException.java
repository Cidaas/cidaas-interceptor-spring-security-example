package de.cidaas.jwt.exceptions;

import de.cidaas.jwt.exceptions.JWTVerificationException;

public class AlgorithmMismatchException extends JWTVerificationException {
    public AlgorithmMismatchException(String message) {
        super(message);
    }
}
