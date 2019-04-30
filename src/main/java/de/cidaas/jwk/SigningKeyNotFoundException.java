package de.cidaas.jwk;



@SuppressWarnings("WeakerAccess")
public class SigningKeyNotFoundException extends JwkException {

    public SigningKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

