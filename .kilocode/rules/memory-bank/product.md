# Java Record Mutator Generator (JRMG) Product Description

* Explains why the project exists
* Describes the problems being solved
* Outlines how the product should work
* User experience goals

## Why

Java records are immutable by design, which is great for thread safety and data integrity. However, they can be
cumbersome to work with when you need to modify nested fields. This project aims to solve that problem by providing a
simple way to create chainable operations to modify nested record fields.

## What

The Java Record Mutator Generator (JRMG) is a compile-time annotation processor that automatically generates fluent
builder classes for Java records. It enables developers to create chainable operations to modify nested record fields
without resorting to complex workarounds or manual builder patterns.
It can generate mutators and constructors for records.

### Mtor (mutator)

A Mtor is a builder that can mutate itself and its nested records and collections. The Mtor can be created from an
existing record or from scratch.
The Mtor enables fluent modification of nested data structurs.

### Ctor (constructor)

A Ctor is a builder that can create a new instance of a record with all fields set. It forces the developer to set all
fields in declaration order and will
generate a compile error if any fields are missing. If a component is added in a record, the Ctor will generate a
compile error, forcing the developer to uppdate all consider what value the new component should have whereever the Ctor
is used.
The Ctor enables fluent creation of nested data structurs.

## How

The JRMG project will be implemented as a compile-time annotation processors. It will analyze Java records annotated with
special annotations and generate fluent builder classes that enable developers to create chainable operations to modify
nested record fields. The generated builders will be type-safe and will provide a clean and intuitive API for working
with nested data structures.

## User Experience Goals

The JRMG project aims to provide a simple and intuitive API for working with nested data structures in Java. The
generated builders should be easy to use and should provide a clean and fluent API for modifying nested record fields.
The project should also provide good error messages and should be easy to integrate into existing projects.
