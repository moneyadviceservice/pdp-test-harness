set -e

if [ "$ENABLE_SSL" != "false" ]
then
  echo "Trying to configure the server key-store for configuring SSL/TLS"
  ./cert-manager.sh createServerKeyStore "/server-cert/server-certificate" "./mTLS"
  echo "Listening for http connections on port $UNSECURE_PORT and for https connections on port $PORT"
else
    echo "SSL/TLS disabled. Listening for http connections on port $UNSECURE_PORT"
fi
exec java -jar stub-cas.jar
