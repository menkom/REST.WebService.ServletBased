package info.mastera.security;

import info.mastera.api.security.IJwtProvider;
import info.mastera.exception.AuthenticationException;
import info.mastera.model.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * JwtProvider is an util class -> it implements useful functions:
 * <p>
 * generate a JWT token
 * validate a JWT token
 * parse username from JWT token
 */
@Component
public class JwtProvider implements IJwtProvider {

    //Messages
    private static final String TOKEN_IS_NULL = "Token is null";
    private static final String TOKEN_CORRUPTED = "Token corrupted";
    private static final String TOKEN_IS_EXPIRED = "Token is expired.";

    //for JWT values
    private static final String ISSUER = "info.mastera";
    private static final String CLAIM_USERTYPE = "utype";

    private static final TemporalAmount TOKEN_VALIDITY = Duration.ofMinutes(30L);
    private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static String encodedKey;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    static {
        generateEncodedKey();
    }

    /**
     * Once per Application life generated encodedKey for secure signing Tokens
     */
    private static void generateEncodedKey() {
        byte[] sharedKey = new byte[32];
        new SecureRandom().nextBytes(sharedKey);
        encodedKey = TextCodec.BASE64.encode(sharedKey);
    }

    /**
     * Method to generate end Date of token actuality
     * according to instant time and time of token life length (TOKEN_VALIDITY)
     *
     * @param date calculates period from this date
     * @return Date of expire
     */
    private static Date getExpirationPeriod(Date date) {
        return Date.from(date.toInstant().plus(TOKEN_VALIDITY));
    }

    private UserType getUserType(Claims claims) {
        return UserType.valueOf(claims.get(CLAIM_USERTYPE, String.class));
    }

    /**
     * Generates token for granted user and password
     */
    @Override
    public String createToken(Authentication authentication) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer(ISSUER)
                .setExpiration(getExpirationPeriod(now))
                .setIssuedAt(now)
                .claim(CLAIM_USERTYPE, authentication.getAuthorities().stream().findFirst())
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(algorithm, encodedKey)
                .compact();
    }

    @Override
    public String getUserName(String token) {
        return Jwts.parser()
                .setSigningKey(encodedKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * Give a UserType according to token
     *
     * @param token JSON Web Token
     * @return UserType as result we receive usertype of enum UserType
     * @throws AuthenticationException Throws exceptions in case of token errors
     */
    @Override
    public UserType getUserType(String token) throws AuthenticationException {
        if (token == null) {
            throw new AuthenticationException(TOKEN_IS_NULL);
        }
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(encodedKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            throw new AuthenticationException(TOKEN_CORRUPTED);
        }

        if (claims.getExpiration().after(new Date())) {
            return getUserType(claims);
        } else {
            throw new AuthenticationException(TOKEN_IS_EXPIRED);
        }
    }

    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println(MessageFormat.format("Invalid JWT signature -> Message: {} ", e));
        } catch (MalformedJwtException e) {
            System.out.println(MessageFormat.format("Invalid JWT token -> Message: {}", e));
        } catch (ExpiredJwtException e) {
            System.out.println(MessageFormat.format("Expired JWT token -> Message: {}", e));
        } catch (UnsupportedJwtException e) {
            System.out.println(MessageFormat.format("Unsupported JWT token -> Message: {}", e));
        } catch (IllegalArgumentException e) {
            System.out.println(MessageFormat.format("JWT claims string is empty -> Message: {}", e));
        }
        return false;
    }
}