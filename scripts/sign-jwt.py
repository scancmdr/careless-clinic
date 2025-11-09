#!/usr/bin/env python3
"""
JWT signing script using ES256 algorithm.

Signs JWT tokens with the private.pem ES256 key.

Usage:
    python3 sign-jwt.py '{"sub": "user123", "name": "John Doe"}'
    python3 sign-jwt.py --exp 3600 '{"sub": "user123"}'
    python3 sign-jwt.py --help

Author: jay
"""

import argparse
import json
import sys
from pathlib import Path
from datetime import datetime, timedelta, timezone

try:
    import jwt
except ImportError:
    print("ERROR: PyJWT library not found. Install with: pip install PyJWT[crypto]", file=sys.stderr)
    sys.exit(1)

try:
    from cryptography.hazmat.primitives import serialization
    from cryptography.hazmat.backends import default_backend
except ImportError:
    print("ERROR: cryptography library not found. Install with: pip install cryptography", file=sys.stderr)
    sys.exit(1)


def load_private_key(key_path):
    """Load ES256 private key from PEM file."""
    try:
        with open(key_path, 'rb') as key_file:
            private_key = serialization.load_pem_private_key(
                key_file.read(),
                password=None,
                backend=default_backend()
            )
        return private_key
    except FileNotFoundError:
        print(f"ERROR: Private key file not found: {key_path}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"ERROR: Failed to load private key: {e}", file=sys.stderr)
        sys.exit(1)


def sign_jwt(payload, private_key, exp_seconds=None):
    """Sign JWT token with ES256 algorithm."""
    headers = {
        "alg": "ES256",
        "typ": "JWT"
    }

    # Add expiration if specified
    if exp_seconds:
        payload["exp"] = datetime.now(timezone.utc) + timedelta(seconds=exp_seconds)

    # Add issued at timestamp if not present
    if "iat" not in payload:
        payload["iat"] = datetime.now(timezone.utc)

    try:
        token = jwt.encode(
            payload,
            private_key,
            algorithm="ES256",
            headers=headers
        )
        return token
    except Exception as e:
        print(f"ERROR: Failed to sign JWT: {e}", file=sys.stderr)
        sys.exit(1)


def main():
    parser = argparse.ArgumentParser(
        description="Sign JWT tokens with ES256 private key",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  %(prog)s '{"sub": "user123", "name": "John Doe"}'
  %(prog)s --exp 3600 '{"sub": "admin", "role": "admin"}'
  %(prog)s --key /path/to/key.pem '{"user": "jay"}'

  # Pipe output to clipboard
  %(prog)s '{"sub": "test"}' | xclip -selection clipboard
        """
    )

    parser.add_argument(
        "payload",
        help="JSON payload to sign (as string or stdin with -)"
    )

    parser.add_argument(
        "--key",
        "-k",
        default=None,
        help="Path to private key PEM file (default: ../private.pem)"
    )

    parser.add_argument(
        "--exp",
        "-e",
        type=int,
        default=None,
        help="Expiration time in seconds from now (optional)"
    )

    parser.add_argument(
        "--pretty",
        "-p",
        action="store_true",
        help="Pretty-print the JWT header and payload for verification"
    )

    args = parser.parse_args()

    # Determine key path
    if args.key:
        key_path = Path(args.key)
    else:
        # Default to ../private.pem relative to script location
        script_dir = Path(__file__).parent
        key_path = script_dir.parent / "private.pem"

    # Load payload
    if args.payload == "-":
        payload_str = sys.stdin.read()
    else:
        payload_str = args.payload

    try:
        payload = json.loads(payload_str)
    except json.JSONDecodeError as e:
        print(f"ERROR: Invalid JSON payload: {e}", file=sys.stderr)
        sys.exit(1)

    if not isinstance(payload, dict):
        print("ERROR: Payload must be a JSON object", file=sys.stderr)
        sys.exit(1)

    # Load private key
    private_key = load_private_key(key_path)

    # Sign JWT
    token = sign_jwt(payload, private_key, args.exp)

    # Output the token
    print(token)

    # Pretty-print if requested
    if args.pretty:
        print("\n--- JWT Header ---", file=sys.stderr)
        header = jwt.get_unverified_header(token)
        print(json.dumps(header, indent=2), file=sys.stderr)

        print("\n--- JWT Payload ---", file=sys.stderr)
        decoded = jwt.decode(token, options={"verify_signature": False})
        print(json.dumps(decoded, indent=2, default=str), file=sys.stderr)


if __name__ == "__main__":
    main()
