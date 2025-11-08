# Use a base image that includes Nginx, ModSecurity, and the OWASP Core Rule Set (CRS)
# This significantly simplifies the build process.
FROM owasp/modsecurity-crs:nginx-alpine

# The Spring Boot application should be deployed as a separate, internal service.
# Replace 'spring-boot-service' with the actual service name of your Spring Boot app in Dokploy.
# We assume the Spring Boot application listens on port 8080 internally.
ENV PROXY_UPSTREAM_HOST=spring-boot-service:8080

# Remove the default Nginx configuration file
RUN rm /etc/nginx/nginx.conf

# Copy our custom Nginx configuration file
# This file will enable ModSecurity and proxy traffic to the Spring Boot app.
COPY nginx.conf /etc/nginx/nginx.conf

# Create necessary directories and set up permissions (best practice)
RUN mkdir -p /var/lib/nginx /var/log/nginx && \
    chown -R nginx:nginx /var/lib/nginx /var/log/nginx

# Nginx with ModSecurity is now listening on port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]