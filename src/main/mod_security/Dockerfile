# Use a base image that includes Nginx, ModSecurity, and the OWASP Core Rule Set (CRS)
# This significantly simplifies the build process.
FROM owasp/modsecurity-crs:nginx-alpine

# The Spring Boot application should be deployed as a separate, internal service.
# Replace 'spring-boot-service' with the actual service name of your Spring Boot app in Dokploy.
# We assume the Spring Boot application listens on port 8080 internally.
ENV PROXY_UPSTREAM_HOST=spring-boot-service:8080

# Use the default unprivileged ports (8080 for HTTP, 8443 for HTTPS)
# The base image runs as unprivileged user and cannot bind to ports below 1024
ENV PORT=8080
ENV SSL_PORT=8443

# The base image uses environment variables to configure ModSecurity
# The base image processes templates in /etc/nginx/templates/ and outputs to /etc/nginx/conf.d/
# Remove any default templates that might conflict
RUN rm -f /etc/nginx/templates/*.template 2>/dev/null || true

# Copy our custom server configuration as a template
# The entrypoint will process this with envsubst and place it in /etc/nginx/conf.d/
# Updated: 2025-11-09 v1.0.26 - Override proxy_backend.conf to fix headers
COPY nginx.conf /etc/nginx/templates/proxy.conf.template

# override this secret file
RUN mkdir -p /etc/nginx/includes
COPY proxy_backend.conf.template /etc/nginx/templates/includes/proxy_backend.conf.template
COPY modsecurity.conf /etc/nginx/conf.d/modsecurity.conf

RUN rm -rf /etc/modsecurity.d/setup.conf
COPY setup.conf /etc/modsecurity.d/setup.conf
RUN cat /etc/modsecurity.d/setup.conf

# Ensure we're root for setup operations
USER root

# Override the default proxy_backend.conf that sets wrong X-Forwarded headers
# This file is included by the base image and overrides our settings
#RUN mkdir -p /etc/nginx/includes && \
#    echo '# Custom proxy headers - always use external HTTPS values' > /etc/nginx/includes/proxy_backend.conf && \
#    echo 'proxy_set_header X-Forwarded-Proto "https";' >> /etc/nginx/includes/proxy_backend.conf && \
#    echo 'proxy_set_header X-Forwarded-Port "443";' >> /etc/nginx/includes/proxy_backend.conf && \
#    echo 'proxy_set_header X-Forwarded-Host $host;' >> /etc/nginx/includes/proxy_backend.conf

# Create necessary directories and set permissions
RUN mkdir -p /var/lib/nginx /var/log/nginx && \
    chown -R nginx:nginx /var/lib/nginx /var/log/nginx /etc/nginx

# Nginx with ModSecurity is now listening on port 8080 (unprivileged)
EXPOSE 8080

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]