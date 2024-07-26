# PDP C&A Stub

This project represents a standalone application that mimics the behaviour of the real C&A and allows performing system tests.

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

The API is exposed as HTTP: there is no support for HTTPS or mTLS protocols at the moment. The default TCP port is 8081.

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

Persistence entities are modeled using JPA and the repositories implement Spring `JpaRepository` for interact with the
database.

### Code generation

The API interfaces code is generated using the Gradle Open API tool.
The task `openApiGenerate` configured in the [build.gradle](build.gradle) file will build under the [generated](generated) dir 
the files based on the contents of the spec configured.

The `api` and `model` packages in [uk/org/ca/stub/simulator/rest](generated/src/main/java/uk/org/ca/stub/simulator/rest/) need to be refactored to be part of the actual
project withing the `uk.org.ca.stub.simulator.rest` package

Links:

* [Gradle plugin documentation](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
* [Spring generator documentation](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md)

### Container Build

The [Dockerfile](Dockerfile) in the project will use a container to build the project as a container.

To build the project, run the command:

```shell
docker build . -t pdp-stub-cas:latest --build-arg API_PORT=8081
```

### Running the Container

For running it, binding the local directory in the operator's file system `casTestLogs` with the container logs 
directory; the application will use it to keep the log files (adjust the port if needed):

```shell
docker run -p 8081:8081 -v ./casTestLogs:/cas/logs pdp-stub-cas:latest
```

The http logs will be also printed in the console.

If you want to use different values for DB location and credentials, you can run it with:
