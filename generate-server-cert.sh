#!/bin/bash
# Generates a self-signed server certificate for running the C&A Stub with SSL/mTLS.
# Outputs server-certificate.pem and server-certificate.key in the current directory.
# Safe to re-run: skips generation if both files already exist.

set -e

CERT_FILE="server-certificate.pem"
KEY_FILE="server-certificate.key"
DAYS_VALID=365
CN="localhost"

if [[ -f "$CERT_FILE" && -f "$KEY_FILE" ]]; then
    echo ">>> $CERT_FILE and $KEY_FILE already exist, skipping generation."
    exit 0
fi

echo ">>> Generating self-signed server certificate (RSA 4096, CN=$CN, valid ${DAYS_VALID} days)..."

MSYS_NO_PATHCONV=1 openssl req \
    -x509 \
    -newkey rsa:4096 \
    -keyout "$KEY_FILE" \
    -out "$CERT_FILE" \
    -days $DAYS_VALID \
    -nodes \
    -subj "/CN=$CN"

echo ">>> Done. Generated:"
echo "      $CERT_FILE"
echo "      $KEY_FILE"
