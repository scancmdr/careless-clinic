package careless.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Utility class providing helper methods for exception handling, timestamps, and JWT validation.
 *
 * @author jay
 */
@Slf4j
public class Tools {
    /**
     * Converts exception stack trace to string.
     *
     * @param e exception to convert
     * @return stack trace as string
     */
    public static String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /** Date-time formatter for timestamps */
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Returns current timestamp as formatted string.
     *
     * @return current time in format "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public static String now() {
        return Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().format(formatter);
    }

    /**
     * Validates JWT token signature against both public keys.
     * <p>
     * Returns true if signature is valid with either {@link #PUBLIC_KEY} or {@link #JAY_PUBLIC_KEY}.
     *
     * @param token JWT token to validate
     * @return true if signature is valid, false otherwise
     */
    public static boolean validateJwtSignature(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        // Try validation with first public key
        if (validateJwtWithKey(token, PUBLIC_KEY)) {
            log.info("JWT validated with PUBLIC_KEY");
            return true;
        }

        // Try validation with second public key
        if (validateJwtWithKey(token, JAY_PUBLIC_KEY)) {
            log.info("JWT validated with JAY_PUBLIC_KEY");
            return true;
        }

        log.warn("JWT signature validation failed with both keys");
        return false;
    }

    /**
     * Validates JWT token with specific public key.
     *
     * @param token JWT token
     * @param publicKeyPem PEM-encoded public key
     * @return true if valid, false otherwise
     */
    private static boolean validateJwtWithKey(String token, String publicKeyPem) {
        try {
            PublicKey publicKey = loadPublicKey(publicKeyPem);

            // Parse and validate the JWT
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (SignatureException e) {
            log.debug("Signature validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.debug("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Loads public key from PEM string.
     *
     * @param publicKeyPem PEM-encoded public key
     * @return loaded public key
     * @throws Exception if key loading fails
     */
    private static PublicKey loadPublicKey(String publicKeyPem) throws Exception {
        String publicKeyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC"); // ES256 uses Elliptic Curve
        return keyFactory.generatePublic(spec);
    }

    /** ES256 public key for JWT validation */
    public static final String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEMU1JFVEO9FkVr0r041GpAWzKvQi1TBYm
            arJj3+aNeC2aK9GT7Hct1OJGWQGbUkNWTeUr+Ui09PjBit+AMYuHgA==
            -----END PUBLIC KEY-----
            """;

    /** Jay's ES256 public key for JWT validation */
    public static final String JAY_PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEdYCSYzUHlSjrjT8ZwHCs54M07C9F
            vU315fz5Ha/pR2ij89LRm+XMVXBwzpLL4XtspW4GhQA24hNiDdCYa2FV5g==
            -----END PUBLIC KEY-----
            """;
}
