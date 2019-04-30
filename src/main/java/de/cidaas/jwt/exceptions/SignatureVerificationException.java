package de.cidaas.jwt.exceptions;

import de.cidaas.jwt.algorithms.Algorithm;
import de.cidaas.jwt.exceptions.JWTVerificationException;

public class SignatureVerificationException extends JWTVerificationException {
    public SignatureVerificationException(Algorithm algorithm) {
        this(algorithm, null);
    }

    public SignatureVerificationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature resulted invalid when verified using the Algorithm: " + algorithm, cause);
    }
}

