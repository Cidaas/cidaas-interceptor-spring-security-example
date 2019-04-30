package de.cidaas.jwt.interfaces;

import de.cidaas.jwt.exceptions.JWTVerificationException;
import de.cidaas.jwt.interfaces.DecodedJWT;


public interface JWTVerifier {
  
  /**
   * Performs the verification against the given Token
   *
   * @param token to verify.
   * @return a verified and decoded JWT.
   * @throws JWTVerificationException if any of the verification steps fail
   */
  DecodedJWT verify(String token) throws JWTVerificationException;

  /**
   * Performs the verification against the given decoded JWT
   *
   * @param jwt to verify.
   * @return a verified and decoded JWT.
   * @throws JWTVerificationException if any of the verification steps fail
   */
  DecodedJWT verify(DecodedJWT jwt) throws JWTVerificationException;
}
