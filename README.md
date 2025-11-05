# The Java Record Mutator Generator 
A java annotation processor that generates mutators for records. The mutators can double as builders.

## Example
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

public static Invoice mutate(Invoice invoice) {
    return new InvoiceMutator(invoice)
            .setCurrency("SEK")
            .mutateBillingAddress(a -> a
                    .setAddressLine1("address 1")
                    .setAddressLine2("address 2"))
            .mutateItems(list -> list
                    .mutate(0, invoiceLine -> invoiceLine
                            .setItemCode("1000")))
            .mutateItems(list -> list
                    .mutateAll((index, item) -> item
                            .setItemCode("100" + index)))
            .build();

}
```
## Gradle
```
dependencies {
    implementation "io.github.larsarv.jrmg:api:1.0.1"
    annotationProcessor "io.github.larsarv.jrmg:annotation-processor:1.0.1"
}
```