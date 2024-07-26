#PLATFORMS="linux/arm64,linux/amd64"
#TAG=cas-stub:multiarch
#docker buildx build . --progress=plain --platform $PLATFORMS -t $TAG --push

ARG API_PORT=8081

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
ARG TARGETPLATFORM
ARG BUILDPLATFORM
EXPOSE ${API_PORT}

RUN echo " :::::::::::::::::::: Running on $BUILDPLATFORM, building for $TARGETPLATFORM ::::::::::::::::::::"
RUN mkdir /cas
RUN chown 1000:1000 -R /cas
USER 1000:1000
ENV HOME=/cas
RUN echo -e '\
0=C&A Stub||jdbc\:h2\:file\:./db/cas|sa\n'\
>> /cas/.h2.server.properties

COPY --from=build /home/gradle/cas/build/libs/*.jar /cas/stub-cas.jar
WORKDIR /cas
ENTRYPOINT ["java", "-jar", "/cas/stub-cas.jar"]
