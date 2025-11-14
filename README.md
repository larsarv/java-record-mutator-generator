# The Java Record Mutator Generator  (JRMG)
## Overview
The Java Record Mutator Generator (JRMG) is a compile-time annotation processor that automatically generates fluent 
mutator/builder classes for Java records. It enables developers to create chainable operations to 
modify nested record fields without resorting to complex workarounds or manual builder patterns.

Records in Java are immutable by default, which makes modifying them cumbersome — especially when dealing with nested 
structures or complex business logic. JRMG solves this problem by generating a mutator class for each annotated record, 
allowing developers to modify nested records in a fluent, builder-like manner.

See [`OVERVIE.md`](OVERVIEW.md) for an overview of the project structure.

## Key Features
- Fluent API: Generated mutators provide a chainable, readable API for modifying record components.
- Immutable Output: All mutations result in a new immutable record instance.
- Nested Record Support: Enabling deep mutation.
- Can act as builders. Can guarantee at compile time that all setters for the record components are called 
  in order of declaration. 
- List, Set, and Map Support: Lists, Sets, and Maps can be mutated using specialized mutator interfaces, 
  allowing you to mutate individual items or apply transformations to all items.
- Compile-Time Generation: Uses annotation processing to generate mutator classes at compile time.
- Type Safety: Fully type-safe — all generated methods are strongly typed and checked at compile time.
- Supports Java 17+

## Use Cases
- Domain Modeling: Modify complex domain records without writing manual builders.
- Testing: Create test data with specific mutations for unit tests.
- Data Transformation: Apply transformations to nested records.

## How It Works
To use JRMG, simply annotate your record class with @GenerateMutator. The annotation processor will generate a mutator 
class with methods to set, get, and mutate each field. The generated class implements RecordMutator<T>, which provides 
a build() method to finalize the mutation and return a new immutable record.
Example:
```
@GenerateMutator
public record Shipment(
        String shipmentNo,
        ShipmentStatus status,
        Map<PartyType,Party> parties,
        List<Parcel> parcels,
        ProformaInvoice proformaInvoice,
        List<String> specialInstructions,
        LocalDateTime createdDate,
        LocalDateTime estimatedDeliveryDate
) {
}
```
This generates `ShipmentMutator`.

Below is an example of using mutators to update nested values.
```
public Shipment updateParcelStatus(Shipment shipment, String parcelNo, ParcelStatus parcelStatus) {
    return ShipmentMutator.mutator(shipment)
            .mutateParcels(parcels -> parcels
                    .findFirstAndMutate(parcel -> parcelNo.equals(parcel.parcelNo()), parcel -> parcel
                            .setStatus(parcelStatus)))
            .build();
}

public Shipment updateShipmentWithNewParcel(Shipment originalShipment, Parcel parcel) {
    return ShipmentMutator.mutator(originalShipment)
            .mutateParcels(parcels -> parcels
                    .add(ParcelMutator.mutator(parcel)
                            .setStatus(ParcelStatus.CREATED)
                            .build()))
            .build();
}
```
Below is an example of using mutators as builder. Note the use off the `all()` function to give compile time error if not all 
component of the record is set in the order of declaration.
```
public Shipment createShipmentTestData() {
    return ShipmentMutator.mutator().all()
            .setShipmentNo("SHP001")
            .setStatus(ShipmentStatus.CREATED)
            .setParties(parties -> parties
                    .put(PartyType.SENDER, party -> party.all()
                            .setName("Acme Corp")
                            .setAddress(address -> address.all()
                                    .setAddress1("123 Main St")
                                    .setAddress2(null)
                                    .setCity("Anytown")
                                    .setState("ST")
                                    .setPostalCode("12345")
                                    .setCountry("US")
                                    .done())
                            .setContactInfo(contactInfos -> contactInfos
                                    .add(contactInfo -> contactInfo.all()
                                            .setType(ContactInfoType.EMAIL)
                                            .setValue("contact@acme.com")
                                            .done())
                                    .add(contactInfo -> contactInfo.all()
                                            .setType(ContactInfoType.PHONE)
                                            .setValue("555-1234")
                                            .done()))
                            .done()) // Remove done?
                    .put(PartyType.RECEIVER, party -> party.all()
                            .setName("John Doe")
                            .setAddress(address -> address.all()
                                    .setAddress1("456 Oak Ave")
                                    .setAddress2(null)
                                    .setCity("Somewhere")
                                    .setState("CA")
                                    .setPostalCode("67890")
                                    .setCountry("US")
                                    .done())
                            .setContactInfo(contactInfos -> contactInfos
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.EMAIL)
                                            .setValue("john.doe@example.com"))
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.PHONE)
                                            .setValue("555-5678")))
                            .done()))
            .setParcels(parcels -> parcels
                    .add(parcel -> parcel.all()
                            .setParcelNo("PARCEL001")
                            .setWeight(new BigDecimal("2.5"))
                            .setLength(new BigDecimal("10.0"))
                            .setWidth(new BigDecimal("8.0"))
                            .setHeight(new BigDecimal("6.0"))
                            .setDescription("Sample Package")
                            .setContents(List.of())
                            .setType(ParcelType.EXPRESS)
                            .setStatus(ParcelStatus.CREATED)
                            .done()))
            .setProformaInvoice(proformaInvoice -> proformaInvoice.all()
                    .setInvoiceNo("INV001")
                    .setDescription("Sample Invoice")
                    .setLineItemPrices(prices -> prices)
                    .setLineItemDescriptions(description -> description)
                    .setQuantities(quantities -> quantities)
                    .setTaxCodes(taxCodes -> taxCodes)
                    .setTotalAmount(new BigDecimal("100.00"))
                    .setIssueDate(LocalDateTime.now())
                    .setCustomFields(customFields -> customFields)
                    .done())
            .setSpecialInstructions(specialInstructions -> specialInstructions
                    .add("Handle with care")
                    .add("Fragile"))
            .setCreatedDate(LocalDateTime.now())
            .setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5))
            .done().build();
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