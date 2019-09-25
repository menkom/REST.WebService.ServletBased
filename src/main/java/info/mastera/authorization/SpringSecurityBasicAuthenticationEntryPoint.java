package info.mastera.authorization;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SpringSecurityBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    private static final String AUTHENTICATE_HEADER = "WWW-Authenticate";
    private static final String REALM_HEADER = "Basic realm=";
    private static final String REALM_NAME = "APPLICATION_REALM";
    private static final String MESSAGE = "Error! ";

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        // If authentication failed, send error response.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader(AUTHENTICATE_HEADER, REALM_HEADER + getRealmName() + "");

        PrintWriter writer = response.getWriter();
        writer.println(MESSAGE + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM_NAME);
        super.afterPropertiesSet();
    }
}