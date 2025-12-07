# Java Record Mutator Generator (JRMG) Overview

The Java Record Mutator Generator (JRMG) is a compile-time annotation processor that automatically generates fluent
mutator and constructor classes for Java records. It enables developers to create chainable operations to modify nested
record
fields and create new records without resorting to complex workarounds or manual builder patterns.

## Subproject Overview

The project has three modules:

### 1. API Module ([`api/`](api/build.gradle))

The API module contains the core interfaces and classes that define the mutator and constructor patterns:

- **Annotations**: `@GenerateMtor`, `@GenerateCtor`, and `@GenerateCtorAndMtor` annotations that mark records for
  mutator and/or constructor generation
- **[`Builder.java`](api/src/main/java/io/github/larsarv/jrmg/api/Builder.java)**: The core interface implemented by all
  generated builders, providing the `build()` method
- **Builder Implementation Classes**: Various classes that handle specific mutator and constructor operations:
    - Collection implementations like [
      `ListBuildImpl.java`](api/src/main/java/io/github/larsarv/jrmg/api/ListBuildImpl.java),
      [`SetBuilderImpl.java`](api/src/main/java/io/github/larsarv/jrmg/api/SetBuilderImpl.java), and
      [`MapBuilderImpl.java`](api/src/main/java/io/github/larsarv/jrmg/api/MapBuilderImpl.java) for collection mutators
      and constructors
    - Specialized interfaces for nested operations like
      [`NestedListMtor.java`](api/src/main/java/io/github/larsarv/jrmg/api/NestedListMtor.java),
      [`SimpleListMtor.java`](api/src/main/java/io/github/larsarv/jrmg/api/SimpleListMtor.java), and their constructor
      counterparts

### 2. Annotation Processor Module ([`annotation-processor/`](annotation-processor/build.gradle))

The annotation processor module contains the core logic that generates mutator and constructor classes at compile time:

- **[
  `AnnotationProcessor.java`](annotation-processor/src/main/java/io/github/larsarv/jrmg/annotation/processor/AnnotationProcessor.java)
  **: The main annotation processor that:
    - Processes records annotated with `@GenerateMtor`, `@GenerateCtor`, or `@GenerateCtorAndMtor`
    - Delegates to `MtorGenerator` and `CtorGenerator` for generating mutator and constructor classes respectively
    - Uses the `TypeManagerFactory` and `TypeManager` hierarchy to handle different types (primitives, records,
      collections)
    - Uses JavaPoet library to generate the mutator and constructor source code

### 3. Example Project ([`example-project/`](example-project/build.gradle))

A demonstration project showing how to use the JRMG library with real-world examples.