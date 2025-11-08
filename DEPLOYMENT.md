# Deployment Guide - Careless Clinic

This guide covers deploying the Careless Health Clinic application to production using Dokploy with Paketo buildpacks.

## Production Environment

- **URL**: https://careless.firebind.com
- **Platform**: Dokploy
- **Buildpack**: Paketo (Java)
- **Database**: PostgreSQL (service container)

## Prerequisites

### 1. Google OAuth2 Setup

Configure your Google OAuth2 application to accept redirect URIs from both local and production:

**Authorized redirect URIs:**
- `http://localhost:8080/login/oauth2/code/google` (for local development)
- `https://careless.firebind.com/login/oauth2/code/google` (for production)

**Required Environment Variables:**
- `GOOGLE_CLIENT_ID` - Your Google OAuth2 Client ID
- `GOOGLE_CLIENT_SECRET` - Your Google OAuth2 Client Secret

### 2. PostgreSQL Database

Ensure your Dokploy PostgreSQL service is configured with:
- Database name: `careless`
- Username: `careless`
- Password: (set via `DB_PASSWORD` environment variable)

## Dokploy Configuration

### Environment Variables

Set the following environment variables in your Dokploy application:

```bash
# Database Configuration
DB_HOST=<your-postgres-service-host>
DB_PORT=5432
DB_NAME=careless
DB_USERNAME=careless
DB_PASSWORD=<your-secure-password>

# OAuth2 Configuration
GOOGLE_CLIENT_ID=<your-google-client-id>
GOOGLE_CLIENT_SECRET=<your-google-client-secret>

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Optional: Logging Configuration
LOG_LEVEL=INFO
SQL_LOG_LEVEL=WARN
JDBC_LOG_LEVEL=WARN
SQL_BINDER_LOG_LEVEL=WARN

# Optional: Hibernate DDL
DDL_AUTO=update

# Port (usually handled by Dokploy)
PORT=8080
```

### Application Settings

1. **Buildpack**: Select Paketo Java buildpack
2. **Build Command**: `mvn clean package -DskipTests`
3. **Start Command**: Handled by `Procfile`
4. **Health Check**: `http://localhost:8080/actuator/health` (if Spring Actuator is added)

## Files for Deployment

The following files support the deployment:

### `Procfile`
Defines how the application starts in production:
```
web: java -Dserver.port=$PORT -Dspring.profiles.active=prod $JAVA_OPTS -jar target/*.jar
```

### `project.toml`
Configures the Paketo buildpack:
- Java version: 21
- Build configuration
- Maven build arguments

### `application-prod.yaml`
Production-specific Spring Boot configuration:
- Optimized database connection pooling
- Reduced logging
- HTTPS/proxy header support
- Production-ready settings

## Local Development

### Running Locally

1. Start PostgreSQL (using Docker):
   ```bash
   ./scripts/postgresql-local.sh
   ```

2. Set environment variables:
   ```bash
   export DB_PASSWORD="your-local-password"
   export GOOGLE_CLIENT_ID="your-google-client-id"
   export GOOGLE_CLIENT_SECRET="your-google-client-secret"
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access at: http://localhost:8080

### Testing with Production Profile Locally

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_PASSWORD="your-local-password"
mvn spring-boot:run
```

## Deployment Checklist

- [ ] PostgreSQL service created in Dokploy
- [ ] Database `careless` created
- [ ] Google OAuth2 configured with production redirect URI
- [ ] All environment variables set in Dokploy
- [ ] Repository connected to Dokploy
- [ ] Buildpack set to Paketo Java
- [ ] SSL/TLS certificate configured for careless.firebind.com
- [ ] Initial deployment tested
- [ ] OAuth2 login flow verified
- [ ] Database connection verified

## Monitoring

### Log Levels

In production (`application-prod.yaml`), logging is set to INFO by default. Adjust via environment variables:

- `LOG_LEVEL` - Application logging (default: INFO)
- `SQL_LOG_LEVEL` - SQL query logging (default: INFO)
- `JDBC_LOG_LEVEL` - JDBC logging (default: INFO)

### Database Health

Check database connection:
```sql
SELECT * FROM text_entries LIMIT 1;
```

## Security Considerations

⚠️ **IMPORTANT**: This application is intentionally vulnerable for educational purposes.

**Never deploy to a real production environment with sensitive data.**

The application demonstrates:
- SQL injection vulnerabilities
- Inadequate access controls
- Missing input validation
- Sensitive data logging

## Troubleshooting

### OAuth2 Redirect Issues

If OAuth2 login fails with redirect URI mismatch:
1. Verify the redirect URI in Google Cloud Console
2. Check that `server.forward-headers-strategy: native` is in production config
3. Ensure Dokploy proxy is passing correct headers

### Database Connection Issues

1. Verify PostgreSQL service is running
2. Check `DB_HOST`, `DB_PORT`, `DB_NAME` environment variables
3. Confirm `DB_PASSWORD` is set correctly
4. Check Dokploy network connectivity between services

### Application Won't Start

1. Check Dokploy logs for errors
2. Verify Java 21 is being used (check build logs)
3. Ensure `SPRING_PROFILES_ACTIVE=prod` is set
4. Verify all required environment variables are present

## Rolling Back

To rollback to a previous version:
1. Go to Dokploy dashboard
2. Select the application
3. Choose "Deployments" tab
4. Click "Rollback" on the desired previous deployment

## Support

For deployment issues specific to this educational application, check:
- CLAUDE.md - Architecture and development guidance
- README.md - Project overview
- Application logs in Dokploy
