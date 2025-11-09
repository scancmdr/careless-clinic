package careless.clinic.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class RequestHeaderLoggerFilter implements Filter {

    public RequestHeaderLoggerFilter() {
        log.info("RequestHeaderLoggerFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.warn("--- Incoming Request Headers ---");
        Collections.list(httpRequest.getHeaderNames()).forEach(headerName ->
                log.warn(headerName + ": " + httpRequest.getHeader(headerName))
        );
        log.warn("--- End Headers ---");
        chain.doFilter(request, response);
    }
}
