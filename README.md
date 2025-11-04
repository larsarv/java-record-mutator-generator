# The Java Record Mutator Generator 
A java annotation processor that generates mutators for records. The mutators can double as builders.

## Example
```
Invoice invoice = new InvoiceMutator()
        .setCurrency("SEK")
        .mutateBillingAddress(a -> a
                .setAddressLine1("Addr 1")
                .setAddressLine2("Addr 2"))
        .mutateItems(list -> list
                .mutate(0, invoiceLine -> invoiceLine
                        .setItemCode("1000")))
                .mutateAll((index, item) -> item
                        .setItemCode("1000" + index)))
        .build();

```
## Gradle
```
dependencies {
    implementation "io.github.larsarv.jrmg:api:1.0.1"
    annotationProcessor "io.github.larsarv.jrmg:annotation-processor:1.0.1"
}

```