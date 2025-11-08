# Use a base image that includes Nginx, ModSecurity, and the OWASP Core Rule Set (CRS)
# This significantly simplifies the build process.
FROM owasp/modsecurity-crs:nginx-alpine

# The Spring Boot application should be deployed as a separate, internal service.
# Replace 'spring-boot-service' with the actual service name of your Spring Boot app in Dokploy.
# We assume the Spring Boot application listens on port 8080 internally.
ENV PROXY_UPSTREAM_HOST=spring-boot-service:8080

# Remove default templates to prevent them from creating unwanted server blocks
# Use || true to ignore errors if directory doesn't exist
RUN rm -rf /etc/nginx/templates/* 2>/dev/null || true

# Ensure templates directory exists and copy our custom Nginx configuration as a template
# The base image's entrypoint will process this template with envsubst
RUN mkdir -p /etc/nginx/templates
COPY nginx.conf /etc/nginx/templates/nginx.conf.template

# Ensure we're root for setup operations
USER root

# Create necessary directories and set permissions
RUN mkdir -p /var/lib/nginx /var/log/nginx && \
    chown -R nginx:nginx /var/lib/nginx /var/log/nginx /etc/nginx

# Nginx with ModSecurity is now listening on port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]