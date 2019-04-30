package de.cidaas.jwk;



@SuppressWarnings("WeakerAccess")
public class InvalidPublicKeyException extends JwkException {

    public InvalidPublicKeyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
