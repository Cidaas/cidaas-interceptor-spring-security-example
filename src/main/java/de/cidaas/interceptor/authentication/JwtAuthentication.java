package de.cidaas.interceptor.authentication;

import de.cidaas.jwt.JWTVerifier;
import de.cidaas.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;

public interface JwtAuthentication {

    String getToken();

    String getKeyId();

    Authentication verify(JWTVerifier verifier) throws JWTVerificationException;
}
