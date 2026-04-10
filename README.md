# PDP C&A Stub

This project represents a standalone application that mimics the behaviour of the real C&A and allows you to perform system tests.

For detailed instructions on setting up and using the C&A Stub, refer to the accompanying admin guide, and to the tester guide.

## Architecture

The application is a [Spring Boot](https://spring.io/microservices) app, using Java 21 and built using Gradle.

Its primary design is to be delivered and run as a container (see the Dockerfile), but can be run directly if desired.

The main functionality of the application is a typical REST API, following the PDP Technical Specification (v1.1)
to stub the behaviour of the C&A.

The controllers and the models have been modeled using the OpenAPI specification (as per the PDP Technical Specification for the C&A), 
available in the [specs](./specs) directory,
using the [OpenAPI Generator project](https://github.com/OpenAPITools/openapi-generator/tree/master).

The application exposes four different endpoints: token, rreguri, perm and introspect.
Note that the reporting (events) endpoint is not supported as this is undergoing change.

The API can be exposed as plain HTTP (default port 8081) or as HTTPS with mTLS (default port 8443). See the [Running the Container](#running-the-container) section for details.

The API actions interact with an embedded database: listing, creating and updating entries.

In addition to controllers, the application exposes a database (H2) console and an OpenAPI console to interact with the system.

## Developer notes

This project requires Java 21 and [Gradle](https://gradle.org/) 

### Starting the service

During development, the C&A Stub can be executed using `gradle` as follows:

```shell
gradle bootRun
```

### Code structure

```
└── uk.org.ca.stub.simulator
    ├── configuration
    │   └── dbinitializer
    ├── entity
    ├── filter
    ├── interceptor
    ├── pojo
    │   └── entity
    ├── repository
    ├── rest
    │   ├── api
    │   ├── controller
    │   ├── exception
    │   └── model
    ├── service
    └── utils
```

The package `uk.org.ca.stub.simulator.rest.*` contains the API files. +
Package `controller` contains the implementation of the interfaces within `api`. +
The content of the `api` and `model` packages are generated using a generator from the Original OpenAPI specification, 
but `controller` is manually created, and it will need to be modified if the interface / the API spec change.

Each controller will rely on a service to implement the stub logic.

Persistence entities are modeled using JPA and the repositories implement Spring `JpaRepository` to interact with the
database.

### Code generation

The API interfaces code is generated using the Gradle Open API tool.
The task `openApiGenerate` configured in the [build.gradle](build.gradle) file will build under the [generated](generated) dir 
the files based on the contents of the spec configured.

The `api` and `model` packages in [uk/org/ca/stub/simulator/rest](generated/src/main/java/uk/org/ca/stub/simulator/rest/) need to be refactored to be part of the actual
project within the `uk.org.ca.stub.simulator.rest` package

Links:

* [Gradle plugin documentation](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
* [Spring generator documentation](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md)

### Container Build

The [Dockerfile](Dockerfile) in the project will use a container to build the project as a container.

To build the project, run the command:

```shell
docker build . -t pdp-stub-cas:latest
```

> **Note:** `API_PORT` is the HTTPS/SSL port (default `8443`). Do not set it to `8081` unless you intend to run with SSL enabled on that port.

### Running the Container

> **Important:** Run all `docker run` commands from the **project root directory**, as the volume mounts use relative paths.

The `casTestLogs` directory is mounted into the container to persist log files. It will be created automatically if it does not exist.

**Without SSL (HTTP only):** exposes HTTP on port 8081.
```shell
docker run -p 8081:8081 -v ./casTestLogs:/cas/logs -e ENABLE_SSL=false pdp-stub-cas:latest
```

**With SSL/mTLS (default):** exposes HTTPS on port 8443 and HTTP on port 8081.

Requires `server-certificate.pem` and `server-certificate.key` to be present in the project root. These files are not committed to the repository (they contain a private key). Generate them once with:

*bash/WSL:*
```shell
./generate-server-cert.sh
```
*PowerShell:*
```powershell
.\generate-server-cert.ps1
```
> **Note:** The PowerShell script requires `openssl` on the PATH (available if Git for Windows or OpenSSL is installed).

The project root is then mounted to `/server-cert` so the startup script can build the server key store from those files.
```shell
docker run -p 8443:8443 -p 8081:8081 -v ./casTestLogs:/cas/logs -v .:/server-cert pdp-stub-cas:latest
```

HTTP logs are also printed to the console.

If you want to use different values for DB location and credentials, you can run it with:
