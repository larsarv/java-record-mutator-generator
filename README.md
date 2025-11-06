# The Java Record Mutator Generator  (JRMG)
## Overview
The Java Record Mutator Generator (JRMG) is a compile-time annotation processor that automatically generates fluent 
mutator classes for Java records. It enables developers to create chainable operations to 
modify record fields without resorting to complex workarounds or manual builder patterns.

Records in Java are immutable by default, which makes modifying them cumbersome — especially when dealing with nested 
structures or complex business logic. JRMG solves this problem by generating a mutator class for each annotated record, 
allowing developers to modify nested records in a fluent, builder-like manner.

## Key Features
- Fluent API: Generated mutators provide a chainable, readable API for modifying record components.
- Immutable Output: All mutations result in a new immutable record instance.
- Nested Record Support: Enabling deep mutation.
- List Support: Lists can be mutated using specialized mutator interfaces (SimpleListMutator and
  MutableRecordListMutator), allowing you to mutate individual records or apply transformations to all items.
- Compile-Time Generation: Uses annotation processing to generate mutator classes at compile time.
- Type Safety: Fully type-safe — all generated methods are strongly typed and checked at compile time.

## Use Cases
- Domain Modeling: Modify complex domain records without writing manual builders.
- Testing: Create test data with specific mutations for unit tests.
- Data Transformation: Apply transformations to nested records or lists of records.

## How It Works
To use JRMG, simply annotate your record class with @GenerateMutator. The annotation processor will generate a mutator 
class with methods to set, get, and mutate each field. The generated class implements RecordMutator<T>, which provides 
a build() method to finalize the mutation and return a new immutable record.
Example:
```
@GenerateMutator
public record Invoice(
        ...
        String currency,
        ...
        Address billingAddress,
        List<InvoiceLineItem> items,
) {
}

@GenerateMutator
public record Address(
        String addressLine1,
        String addressLine2,
        ...
) {
}

@GenerateMutator
public record InvoiceLineItem(
    String itemCode,
    ...
) {
}
```
This generates `InvoiceMutator`, `Address` and `InvoiceLineItem`, which you can use like:
```
public static Invoice mutate(Invoice invoice) {
    return new InvoiceMutator(invoice)
            .setCurrency("SEK")
            .mutateBillingAddress(a -> a
                    .setAddressLine1("address 1")
                    .setAddressLine2("address 2"))
            .mutateItems(list -> list
                    .mutateAll((index, item) -> item
                            .setItemCode("100" + index)))
            .build();

}
```
## Gradle Setup
```
ext {
    JRMG_VERSION = "1.0.0" // Use the latest version
}

dependencies {
    implementation "io.github.larsarv.jrmg:api:$JRMG_VERSION"
    annotationProcessor "io.github.larsarv.jrmg:annotation-processor:$JRMG_VERSION"
}
```

## Maven Setup
```
<properties>
    <jrmg.version>1.0.0</jrmg.version>
</properties>

<dependencies>
    <dependency>
        <groupId>io.github.larsarv.jrmg</groupId>
        <artifactId>api</artifactId>
        <version>${jrmg.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.larsarv.jrmg</groupId>
                        <artifactId>annotation-processor</artifactId>
                        <version>${jrmg.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Conclusion
The Java Record Mutator Generator is a powerful tool for developers who want to leverage Java records while 
maintaining the ability to modify them in a fluent, immutable, and type-safe way. By combining the simplicity of 
records with the power of fluent APIs, JRMG reduces boilerplate and improves code readability.
If you’re using records and want to avoid manual builders, JRMG is your solution.