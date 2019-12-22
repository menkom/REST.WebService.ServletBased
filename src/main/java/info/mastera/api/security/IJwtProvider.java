package info.mastera.api.security;

import info.mastera.exception.AuthenticationException;
import info.mastera.model.UserType;
import org.springframework.security.core.Authentication;

public interface IJwtProvider {

    String createToken(Authentication authentication);

    String getUserName(String token);

    UserType getUserType(String token) throws AuthenticationException;

    boolean validateJwtToken(String authToken);
}