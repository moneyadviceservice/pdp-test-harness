#!/bin/bash
# USAGE:
#    ./cert-manager.sh [command] [command parameters]
#
# DESCRIPTION
#    This is a helper script for creating server and client certificates and an example crypto pack
#
# COMMANDS
#   createServerKeyStore [server certificate base path] [key-store path]
#   createServerCerts [CA path] [cert CN]
#   createClientCerts [CA path] [cert CN]
#   createCert [CA path] [cert CN] [output cert path]
#   createCryptoPack [rootCA PEM] [intermediateCA PEM] [client cert PEM] [output crypto-pack path]
#   testCryptoPack [crypto-pack path] [domain] [port]
#

set -e

KEY_STORE_PASSWORD="${KEY_STORE_PASSWORD:-keyStoreSecret}"

function help () {
    HELP_LINES_COUNT=15 # Update this value with the number of lines to print as help
    head -$HELP_LINES_COUNT $0 | sed "/!\/bin\/bash/d" | sed -e "s/#//g"
}


function createServerKeyStore() {
  CERT_FILE_NAME="${1}"
  if [[ ! -f $CERT_FILE_NAME.pem ]]
   then
     echo ">>> Server certificate not fount on expected path: ${CERT_FILE_NAME}.pem"
     exit 1
  fi
  if [[ ! -f $CERT_FILE_NAME.key ]]
   then
     echo ">>> Server certificate key not fount on expected path: ${CERT_FILE_NAME}.key"
     exit 1
  fi

  if [ -n "${2}" ]
  then
    KEY_STORE_PATH="${2}"
  else
    KEY_STORE_PATH="keystore"
  fi

  echo ">>> Adding certificate on $CERT_FILE_NAME to $KEY_STORE_PATH/server-key-store.p12"

  mkdir -p $KEY_STORE_PATH
  openssl pkcs12 \
    -export \
    -in $CERT_FILE_NAME.pem \
    -inkey $CERT_FILE_NAME.key \
    -out $KEY_STORE_PATH/server-key-store.p12 \
    -name "server-certificate" \
    -passout pass:$KEY_STORE_PASSWORD
}

function removeCerts() {
  echo ">>> Removing all certificate"
  rm -rf "${1}"
}

function createServerCerts(){
  if [ -z "${1}" ]
    then
      echo ">>> No required issuing CA path"
      exit 1
  fi
  CA_PATH="${1}"
  CERT_FILE_NAMES="server"
  if [ -z "${2}" ]
    then
      DEFAULT_SERVER_CN="localhost"
      echo ">>> No client CN passed, defaulting to '$DEFAULT_SERVER_CN'"
      CERT_CN=$DEFAULT_SERVER_CN
    else
      CERT_CN="${2}"
  fi
  removeCerts "$CERT_FILE_NAMES"
  createCert "$CA_PATH" "$CERT_CN" "$CERT_FILE_NAMES"
}

function createClientCerts() {
  if [ -z "${1}" ]
    then
      echo ">>> No required issuing CA path"
      exit 1
  fi
  CA_PATH="${1}"
  CERT_FILE_NAMES=client
  if [ -z "${2}" ]
    then
      DEFAULT_CLIENT_CN="client"
      echo ">>> No client CN passed, defaulting to '$DEFAULT_CLIENT_CN'"
      CERT_CN=$DEFAULT_CLIENT_CN
    else
      CERT_CN="${2}"
  fi
  removeCerts "$CERT_FILE_NAMES"
  createCert "$CA_PATH" "$CERT_CN" "$CERT_FILE_NAMES"
}

function createCert(){
  if [ -z "${1}" ]
    then
      echo ">>> No required issuing CA path"
      exit 1
  fi
  CA_PATH="${1}"
  if [ -z "${2}" ]
    then
      echo ">>> No CN passed, defaulting to 'localhost'"
      CERT_CN=localhost
    else
      CERT_CN="${2}"
  fi
  if [ -z "${3}" ]
    then
      echo ">>> No cert path, defaulting to 'cert'"
      CERT_PATH=cert
    else
      CERT_PATH="${3}"
  fi

  echo ">>> Creating certificate files for '$CERT_CN' in '${CERT_PATH}'"
  mkdir -p "$CERT_PATH"
  CERT_FILES_NAME="$CERT_PATH"
  echo ">>> CERT_FILES_NAME ${CERT_FILES_NAME}"

  openssl ecparam -genkey -name prime256v1 -out $CERT_PATH/$CERT_FILES_NAME.key -noout
  openssl req \
    -new \
    -key $CERT_PATH/$CERT_FILES_NAME.key \
    -out $CERT_PATH/$CERT_FILES_NAME.csr \
    -nodes \
    -subj "/CN=$CERT_CN"
  echo ">>> Signing certificate"
  openssl pkcs8 -topk8 -inform pem -in $CERT_PATH/$CERT_FILES_NAME.key -outform pem -nocrypt -out $CERT_PATH/$CERT_FILES_NAME.key.pem
  openssl ec -in $CERT_PATH/$CERT_FILES_NAME.key -pubout -out $CERT_PATH/$CERT_FILES_NAME.pub.key
  openssl ca \
    -batch \
    -config $CA_PATH/openssl.conf \
    -keyfile $CA_PATH/private/subordinateCA.key \
    -passin pass:serverNTERMEDIATE_CA_PASS \
    -cert $CA_PATH/certs/subordinateCA.crt \
    -out $CERT_PATH/$CERT_FILES_NAME.crt \
    -infiles $CERT_PATH/$CERT_FILES_NAME.csr

  echo ">>> Converting certificates to PEM"
  openssl x509 \
  -in $CERT_PATH/$CERT_FILES_NAME.crt \
  -out $CERT_PATH/$CERT_FILES_NAME.pem
}

function create_crypto_pack(){
  ROOT_CA_PATH="${1}"
  INTERMEDIATE_CA_PATH="${2}"
  CLIENT_CERT_PATH="${3}"
  EXAMPLE_CRYPTO_PACK_PATH="${4}"

  # copy cert files
  mkdir -p "$EXAMPLE_CRYPTO_PACK_PATH"
  cp "$CLIENT_CERT_PATH".key.pem "$EXAMPLE_CRYPTO_PACK_PATH"/certPrivateKey
  cp "$CLIENT_CERT_PATH".pub.key "$EXAMPLE_CRYPTO_PACK_PATH"/certPublicKey
  cp "$CLIENT_CERT_PATH".pem "$EXAMPLE_CRYPTO_PACK_PATH"/certificate

  # create certificateChain
  cat $INTERMEDIATE_CA_PATH $ROOT_CA_PATH  > "$EXAMPLE_CRYPTO_PACK_PATH"/certificateChain

  cp ../crypto-pack-for-system-testing/jwtPrivateKey "$EXAMPLE_CRYPTO_PACK_PATH"/jwtPrivateKey
  cp ../crypto-pack-for-system-testing/jwtPublicKey "$EXAMPLE_CRYPTO_PACK_PATH"/jwtPublicKey
  cp ../crypto-pack-for-system-testing/kid "$EXAMPLE_CRYPTO_PACK_PATH"/kid

}

function testCryptoPack() {
    if [ -z "${2}" ]
      then
        echo ">>> No C&A Stub domain passed, defaulting to 'localhost'"
        CERT_CN=localhost
      else
        CERT_CN=${2}
    fi

    if [ -z "${3}" ]
      then
        echo ">>> No C&A Stub por, defaulting to '8443'"
        PORT=8443
      else
        PORT=${3}
    fi

  curl \
    --cacert "${1}"/certificateChain \
    --cert-type PEM \
    --cert "${1}"/certificate \
    --key "${1}"/certPrivateKey \
    --header "X-Request-ID: AE46BBEB-F4B2-40BA-8763-8B091E84CE3B" \
    --verbose \
    https://$CERT_CN:$PORT/jwk_uri

}

case "$1" in
createServerKeyStore) # server certificate | key-store path
  createServerKeyStore $2 $3 # server certificate base path | key-store path
  ;;
createServerCerts) # CA path | cert CN
  createServerCerts $2 $3 # CA path | cert CN
  ;;
createClientCerts) # CA path | cert CN
  createClientCerts $2 $3 # CA path | cert CN
  ;;
createCert) # CA path | cert CN | output cert path
  createCert $2 $3 $4 # CA path | cert CN | output cert path
  ;;
createCryptoPack) # rootCA PEM | intermediateCA PEM | client cert PEM | output crypto-pack path
   create_crypto_pack $2 $3 $4 $5 # rootCA PEM | intermediateCA PEM | client cert PEM | output crypto-pack path
  ;;
testCryptoPack) # crypto-pack path | domain | port
  testCryptoPack $2 $3 $4 # crypto-pack path | domain | port
  ;;
*) help;;
esac
