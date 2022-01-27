# Eclipse Ditto reconciler

This application helps in reconciling desired vs actual state when using the Eclipse Ditto digital twin platform.

The logic used is:

  * Ditto events are read from a Kafka topic, exported by a Ditto Kafka connection
    * Change events must be normalized, and have "extraFields" for the properties which should be reconciled
    * It is possible to use filters
  * When a change events comes along it will compare the desired state to the actual state
  * If there is a discrepancy, it will send a command to Drogue Cloud in the form of:
    * Thing ID mapped to Application/Device
    * Command name is the feature
    * Command payload is the desired property value

Possible TODOs:

* [ ] Rate limit the commands
* [ ] Delete the desired property one it was confirmed

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ditto-reconciler-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
