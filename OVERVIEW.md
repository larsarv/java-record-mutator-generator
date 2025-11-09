# Java Record Mutator Generator (JRMG) Overview

The Java Record Mutator Generator (JRMG) is a compile-time annotation processor that automatically generates fluent mutator/builder classes for Java records. It enables developers to create chainable operations to modify nested record fields without resorting to complex workarounds or manual builder patterns.

## Subproject Overview

The project has three modules:

### 1. API Module ([`api/`](api/build.gradle))
The API module contains the core interfaces and classes that define the mutator pattern:

- **[`GenerateMutator.java`](api/src/main/java/io/github/larsarv/jrmg/api/GenerateMutator.java)**: A simple annotation that marks records for mutator generation
- **[`Mutator.java`](api/src/main/java/io/github/larsarv/jrmg/api/Mutator.java)**: The core interface implemented by all generated mutators, providing the `build()` method
- **Mutator Implementation Classes**: Various classes that handle specific mutator operations:
    - [`ListMutatorImpl.java`](api/src/main/java/io/github/larsarv/jrmg/api/ListMutatorImpl.java) and [`SetMutatorImpl.java`](api/src/main/java/io/github/larsarv/jrmg/api/SetMutatorImpl.java) for collection mutators
    - Specialized function interfaces like [`SimpleListMutateFunction.java`](api/src/main/java/io/github/larsarv/jrmg/api/SimpleListMutateFunction.java), [`NestedListMutateFunction.java`](api/src/main/java/io/github/larsarv/jrmg/api/NestedListMutateFunction.java), etc.

### 2. Annotation Processor Module ([`annotation-processor/`](annotation-processor/build.gradle))
The annotation processor module contains the core logic that generates mutator classes at compile time:

- **[`AnnotationProcessor.java`](annotation-processor/src/main/java/io/github/larsarv/jrmg/annotation/processor/AnnotationProcessor.java)**: The main annotation processor that:
    - Processes records annotated with [`@GenerateMutator`](api/src/main/java/io/github/larsarv/jrmg/api/GenerateMutator.java)
    - Generates mutator classes with setters, getters, and mutation methods
    - Handles nested records, lists, and sets with appropriate mutator implementations
    - Uses JavaPoet library to generate the mutator source code

### 3. Example Project ([`example-project/`](example-project/build.gradle))
A demonstration project showing how to use the JRMG library with real-world examples.