Features

  1. Signs JWTs with ES256 using your private.pem key
  2. Automatic timestamps - adds iat (issued at) automatically
  3. Optional expiration - use --exp to set expiration in seconds
  4. Pretty-print mode - use --pretty to see the decoded header and payload
  5. Flexible key location - defaults to ../private.pem but can be overridden with --key

  Usage Examples

  # Basic usage
  python3 scripts/sign-jwt.py '{"sub": "user123", "name": "John Doe"}'

  # With 1-hour expiration
  python3 scripts/sign-jwt.py --exp 3600 '{"sub": "admin", "role": "admin"}'

  # Pretty-print to see the decoded token
  python3 scripts/sign-jwt.py --pretty '{"sub": "jay"}'

  # Use a different key
  python3 scripts/sign-jwt.py --key /path/to/key.pem '{"user": "jay"}'

  Requirements

  The script requires Python 3 with these libraries:
  - PyJWT[crypto] - for JWT encoding
  - cryptography - for ES256 key handling

  Install with:
  pip install PyJWT[crypto] cryptography
