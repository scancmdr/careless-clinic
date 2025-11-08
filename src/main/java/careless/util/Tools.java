package careless.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tools {
    public static String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static String now() {
        return Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime().format(formatter);
    }

    // ES256
    public static final String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEMU1JFVEO9FkVr0r041GpAWzKvQi1TBYm
            arJj3+aNeC2aK9GT7Hct1OJGWQGbUkNWTeUr+Ui09PjBit+AMYuHgA==
            -----END PUBLIC KEY-----
            """;
    public static final String JAY_PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEdYCSYzUHlSjrjT8ZwHCs54M07C9F
            vU315fz5Ha/pR2ij89LRm+XMVXBwzpLL4XtspW4GhQA24hNiDdCYa2FV5g==
            -----END PUBLIC KEY-----
            """;
}
