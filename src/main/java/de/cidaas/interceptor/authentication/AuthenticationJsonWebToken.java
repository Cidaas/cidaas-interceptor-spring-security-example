package de.cidaas.interceptor.authentication;

import de.cidaas.jwt.JWT;
import de.cidaas.jwt.JWTVerifier;
import de.cidaas.jwt.exceptions.JWTVerificationException;
import de.cidaas.jwt.interfaces.Claim;
import de.cidaas.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthenticationJsonWebToken implements Authentication, JwtAuthentication {

	private final DecodedJWT decoded;
	private boolean authenticated;

	AuthenticationJsonWebToken(String token, JWTVerifier verifier) throws JWTVerificationException {
		this.decoded = verifier == null ? JWT.decode(token) : verifier.verify(token);
		this.authenticated = verifier != null;
	}

	@Override
	public String getToken() {
		return decoded.getToken();
	}

	@Override
	public String getKeyId() {
		return decoded.getKeyId();
	}

	@Override
	public Authentication verify(JWTVerifier verifier) throws JWTVerificationException {
		return new AuthenticationJsonWebToken(getToken(), verifier);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		final String[] scopes = decoded.getClaim("scopes").asArray(String.class);
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		if (scopes != null && scopes.length > 0) {
			for (String value : scopes) {
				authorities.add(new SimpleGrantedAuthority(value));
			}
		}				
		
		final String[] roles = decoded.getClaim("roles").asArray(String.class);

		if (roles != null && roles.length > 0) {
			for (String value : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_"+value));
			}
		}
		
		if(authorities!=null && authorities.size() > 0) {
			return authorities;	
		}else {
			return new ArrayList<GrantedAuthority>();
		}
		
	}

	@Override
	public Object getCredentials() {
		return decoded.getToken();
	}

	@Override
	public Object getDetails() {
		return decoded;
	}

	@Override
	public Object getPrincipal() {
		return decoded.getSubject();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Must create a new instance to specify that the authentication is valid");
		}
		this.authenticated = false;
	}

	@Override
	public String getName() {
		return decoded.getSubject();
	}
}
