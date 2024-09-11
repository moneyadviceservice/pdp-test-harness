# `cURL` requesti

```shell
PORT=8443
DNS_NAME=casstubserver
# REQUEST_ID it could be generated calling `uuidgen` and moved to each curl block if uniqueness is required
REQUEST_ID="beefff7-d885-40d8-8c58-103cb52aa867"
CA_PATH=../crypto-pack-for-system-testing/certificateChain
CLIENT_CERT=../crypto-pack-for-system-testing/certificate
CLIENT_CERT_KEY=../crypto-pack-for-system-testing/certPrivateKey
WRONG_CLIENT_CERT_BASE_PATH=wrong-client/client
```
## PEM certificates

```shell
# Correct scenario
curl \
  --cacert $CA_PATH \
  --cert-type PEM \
  --cert $CLIENT_CERT \
  --key $CLIENT_CERT_KEY \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```

```shell
# Correct scenario ignoring server cert
curl \
  --insecure \
  --cert-type PEM \
  --cert $CLIENT_CERT \
  --key $CLIENT_CERT_KEY \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```

```shell
# Expecting failing due to not trusted client
curl \
  --cacert $CA_PATH \
  --cert-type PEM \
  --cert $WRONG_CLIENT_CERT_BASE_PATH.pem \
  --key $WRONG_CLIENT_CERT_BASE_PATH.key.pem \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```

```shell
# Expecting failing due to the wrong DNS name
curl \
  --cacert $CA_PATH \
  --cert $CLIENT_CERT \
  --key $CLIENT_CERT_KEY \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose https://127.0.0.1:$PORT/jwk_uri
```

```shell
# Expecting failing without the client cert
curl \
  --cacert $CA_PATH \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```

```shell
# Expecting failing without the server cert
curl \
  --cert-type PEM \
  --cert $CLIENT_CERT \
  --key $CLIENT_CERT_KEY \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```

```shell
# Expecting failing without the server and client certs
curl \
  --header "X-Request-ID: $REQUEST_ID" \
  --verbose \
  https://$DNS_NAME:$PORT/jwk_uri
```
