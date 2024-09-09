#!/bin/bash
# USAGE:
#    ./ca-manager.sh [command] [command parameters]
# DESCRIPTION
#    This is a helper script for creating a root and subordinate CAs for testing purposes. It also create a C&A Stub
#    compatible trust-store
#
# COMMANDS
#   createRootCa [rootCA_path]
#   createSubordinateCa [rootCA_path] [subordinateCA_path]
#   createTrustStore [subordinateCA_path] [trust-store_path]
#

set -e

ROOT_CA_PASS="${ROOT_CA_PASS:-rootCaSecret}"
SUBORDINATE_CA_PASS="${SUBORDINATE_CA_PASS:-subordinateCaSecret}"
TRUST_STORE_PASSWORD="${TRUST_STORE_PASSWORD:-trustStoreSecret}"

function help () {
    HELP_LINES_COUNT=12 # Update this value with the number of lines to print as help
    head -$HELP_LINES_COUNT $0 | sed "/!\/bin\/bash/d" | sed -e "s/#//g"
}

function deleteCa(){
  echo ">>> Deleting ${1}"
  rm -rf "${1}"
}

function createCaStruct(){
  C="${1}"
  mkdir -p $C
  pushd $C
  mkdir -p certs crl newcerts private
  popd
  echo 1000 > $C/serial
  touch $C/index.txt $C/index.txt.attr

  echo '
  [ ca ]
  default_ca = CA_default
  [ CA_default ]
  dir            = '$C'                     # Where everything is kept
  certs          = $dir/certs               # Where the issued certs are kept
  crl_dir        = $dir/crl                 # Where the issued crl are kept
  database       = $dir/index.txt           # database index file.
  new_certs_dir  = $dir/newcerts            # default place for new certs.
  certificate    = $dir/cacert.pem          # The CA certificate
  serial         = $dir/serial              # The current serial number
  crl            = $dir/crl.pem             # The current CRL
  private_key    = $dir/private/ca.key.pem  # The private key
  RANDFILE       = $dir/.rnd                # private random number file
  nameopt        = default_ca
  certopt        = default_ca
  policy         = policy_match
  default_days   = 3650
  default_md     = sha256

  [ policy_match ]
  countryName            = optional
  stateOrProvinceName    = optional
  organizationName       = optional
  organizationalUnitName = optional
  commonName             = supplied
  emailAddress           = optional

  [req]
  req_extensions = v3_req
  distinguished_name = req_distinguished_name

  [req_distinguished_name]

  [v3_req]
  basicConstraints = CA:TRUE
  ' > $C/openssl.conf
}

function createRootCa(){
  ROOT_CA_PATH="${1}"
  createCaStruct "$ROOT_CA_PATH"
  echo ">>> Creating root CA on dir $ROOT_CA_PATH"
    openssl ecparam -genkey -name prime256v1 -out $ROOT_CA_PATH/private/rootCA.pkcs.key -noout
    mv $ROOT_CA_PATH/private/rootCA.pkcs.key $ROOT_CA_PATH/private/rootCA.key

    openssl req \
      -config "$ROOT_CA_PATH"/openssl.conf \
      -new \
      -x509 \
      -days 3650 \
      -key "$ROOT_CA_PATH"/private/rootCA.key \
      -sha256 \
      -extensions v3_req \
      -out "$ROOT_CA_PATH"/certs/rootCA.crt \
      -subj '/C=GB/O=PensionsDashboardsProgramme/OU=PKI/CN=Pensions Dashboards Programme - CAS Stub Root CA' \
      -passout pass:$ROOT_CA_PASS

    openssl x509 \
      -in "$ROOT_CA_PATH"/certs/rootCA.crt \
      -out "$ROOT_CA_PATH"/certs/rootCA.pem
}

function createSubordinateCa() {
  ROOT_CA_PATH="${1}"
  SUBORDINATE_CA_PATH="${2}"
  echo ">>> Using root CA on dir $ROOT_CA_PATH"
  echo ">>> Creating subordinate CA on dir $SUBORDINATE_CA_PATH"
  createCaStruct $SUBORDINATE_CA_PATH
  echo ">>> Creating intermediate CA"
    openssl ecparam -genkey -name prime256v1 -out $SUBORDINATE_CA_PATH/private/subordinateCA.pkcs.key -noout
    mv $SUBORDINATE_CA_PATH/private/subordinateCA.pkcs.key $SUBORDINATE_CA_PATH/private/subordinateCA.key

    openssl req \
      -config $SUBORDINATE_CA_PATH/openssl.conf \
      -sha256 \
      -new \
      -key $SUBORDINATE_CA_PATH/private/subordinateCA.key \
      -out $SUBORDINATE_CA_PATH/certs/subordinateCA.csr \
      -subj '/C=GB/O=PensionsDashboardsProgramme/OU=PKI/CN=Pensions Dashboards Programme - CAS Stub Intermediate CA' \
      -passout pass:$SUBORDINATE_CA_PASS

    openssl ca \
      -batch \
      -config $ROOT_CA_PATH/openssl.conf \
      -keyfile $ROOT_CA_PATH/private/rootCA.key \
      -cert $ROOT_CA_PATH/certs/rootCA.crt \
      -extensions v3_req \
      -notext \
      -md sha256 \
      -in $SUBORDINATE_CA_PATH/certs/subordinateCA.csr \
      -out $SUBORDINATE_CA_PATH/certs/subordinateCA.crt
    openssl x509 \
      -in $SUBORDINATE_CA_PATH/certs/subordinateCA.crt \
      -out $SUBORDINATE_CA_PATH/certs/subordinateCA.pem
}

function createTrustStore() {
  SUBORDINATE_CA_PATH="${1}"
  TRUST_STORE_PATH_CA_PATH="${2}"
  echo ">>> Importing subordinateCA $SUBORDINATE_CA_PATH cert into trust store on dir $TRUST_STORE_PATH_CA_PATH"
   mkdir -p $TRUST_STORE_PATH_CA_PATH
   keytool \
     -import \
     -file $SUBORDINATE_CA_PATH/certs/subordinateCA.pem \
     -alias "subordinateCA-certificate" \
     -keystore $TRUST_STORE_PATH_CA_PATH/server-trust-store.p12 \
     -storetype PKCS12 \
     -storepass $TRUST_STORE_PASSWORD \
     -noprompt
}

case "$1" in
createRootCa) #rootCA path
    deleteCa "$2" #rootCA path
    createRootCa "$2" #rootCA path
    ;;
createSubordinateCa) #rootCA path | subordinateCA path
    deleteCa "$3" # subordinateCA path
    createSubordinateCa "$2" "$3" #rootCA path | subordinateCA path
    ;;
createTrustStore) #subordinateCA path | trust-store path
  createTrustStore "$2" "$3" #subordinateCA path | trust-store path
  ;;
*) help;;
esac
