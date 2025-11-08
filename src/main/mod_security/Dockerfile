# Use a base image that includes Nginx, ModSecurity, and the OWASP Core Rule Set (CRS)
# This significantly simplifies the build process.
FROM owasp/modsecurity-crs:nginx-alpine

# The Spring Boot application should be deployed as a separate, internal service.
# Replace 'spring-boot-service' with the actual service name of your Spring Boot app in Dokploy.
# We assume the Spring Boot application listens on port 8080 internally.
ENV PROXY_UPSTREAM_HOST=spring-boot-service:8080

# The base image uses environment variables to configure ModSecurity
# Use conf.d approach instead of replacing the entire nginx.conf
# Remove any default server configs that might conflict
RUN rm -f /etc/nginx/conf.d/default.conf 2>/dev/null || true

# Copy our custom server configuration to conf.d
# This will be included by the main nginx.conf from the base image
COPY nginx.conf /etc/nginx/conf.d/proxy.conf

# Ensure we're root for setup operations
USER root

# Create necessary directories and set permissions
RUN mkdir -p /var/lib/nginx /var/log/nginx && \
    chown -R nginx:nginx /var/lib/nginx /var/log/nginx /etc/nginx

# Nginx with ModSecurity is now listening on port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]