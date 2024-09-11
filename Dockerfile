#PLATFORMS="linux/arm64,linux/amd64"
#TAG=cas-stub:multiarch
#docker buildx build . --progress=plain --platform $PLATFORMS -t $TAG --push

ARG API_PORT=8443
ARG UNSECURE_API_PORT=8081

FROM --platform=$BUILDPLATFORM gradle:8.8-jdk21 AS build
ARG TARGETPLATFORM
ARG BUILDPLATFORM
RUN echo " :::::::::::::::::::: Running on $BUILDPLATFORM, building for $TARGETPLATFORM ::::::::::::::::::::"

WORKDIR /home/gradle/cas
COPY --chown=root:root --chmod=755 src src/
COPY --chown=root:root --chmod=755 settings.gradle settings.gradle
COPY --chown=root:root --chmod=755 build.gradle build.gradle
RUN gradle --no-daemon

FROM amazoncorretto:21-al2023
# Alternative base images:
# * amazoncorretto:21-alpine
# * alpine/java:21-jdk
ARG API_PORT
ARG UNSECURE_API_PORT
ARG TARGETPLATFORM
ARG BUILDPLATFORM

EXPOSE ${API_PORT} ${UNSECURE_API_PORT}

COPY --chown=root:root --chmod=755 certificate-mgmt/cert-manager.sh /cas/
COPY --chown=root:root --chmod=755 DockerEntrypoint/start_stub_wrapper.sh /cas/start_stub_wrapper.sh
COPY --chown=root:root --chmod=555 mTLS/server-trust-store.p12 /cas/mTLS/server-trust-store.p12
# Omiting `server-key-store.p12` a is expeted to be generated from a provided certificate
#COPY --chown=root:root --chmod=755 mTLS/server-key-store.p12 /cas/mTLS/server-key-store.p12

RUN echo " :::::::::::::::::::: Running on $BUILDPLATFORM, building for $TARGETPLATFORM ::::::::::::::::::::" \
 && chown 1000:1000 -R /cas \
 && yum update \
 && yum install -y openssl

USER 1000:1000
ENV PORT=${API_PORT}
ENV UNSECURE_PORT=${UNSECURE_API_PORT}
ENV HOME=/cas
RUN echo -e '\
0=C&A Stub||jdbc\:h2\:file\:./db/cas|sa\n'\
>> /cas/.h2.server.properties

COPY --from=build /home/gradle/cas/build/libs/*.jar /cas/stub-cas.jar
WORKDIR /cas

ENTRYPOINT ["/bin/bash", "-c", "/cas/start_stub_wrapper.sh"]
