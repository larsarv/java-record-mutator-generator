# Technologies & Tools

## Core Technologies

* **Java 17:** The project is built using Java 17. This version was chosen to support modern Java features, particularly
  Records (introduced in Java 14/16), which are the primary target of this library.
* **Gradle:** The build automation tool used for the project. It manages dependencies, compilation, testing, and
  publishing.

## Libraries & Frameworks

* **JavaPoet (com.palantir.javapoet):** A Java API for generating `.java` source files. It is used extensively in the
  `annotation-processor` module to construct the mutator classes programmatically.
* **AutoService (com.google.auto.service):** A configuration generator for `java.util.ServiceLoader`-style service
  providers. It is used to automatically register the `AnnotationProcessor` in the
  `META-INF/services/javax.annotation.processing.Processor` file.
* **JUnit 5:** The testing framework used for unit and integration tests.

## Development Setup

### Prerequisites

* JDK 17 or higher
* Gradle (wrapper provided)

### Build Commands

* **Build:** `./gradlew build` - Compiles and tests all modules.
* **Test:** `./gradlew test` - Runs the test suite.
* **Clean:** `./gradlew clean` - Removes build artifacts.

## Project Structure

The project is a multi-module Gradle build:

* **`api`**: Contains the annotations and runtime interfaces. This is a lightweight dependency for users of the library.
* **`annotation-processor`**: Contains the compiler plugin logic. This is a compile-time only dependency.
* **`example-project`**: A sample application demonstrating how to use the library.

## Technical Constraints

* **Java Version:** The library requires Java 17+ because it relies on Java Records.
* **Annotation Processing:** The solution depends on the Java compiler's annotation processing facility. It must run
  during the compilation phase.