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
- Can act as builders.
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

Example of usage of mutators below.
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

public Shipment createShipmentTestData() {
    return ShipmentMutator.mutator()
            .setShipmentNo("SHP001")
            .setStatus(ShipmentStatus.CREATED)
            .mutateParties(parties -> parties
                    .put(PartyType.SENDER, party -> party
                            .setName("Acme Corp")
                            .mutateAddress(address -> address
                                    .setAddress1("123 Main St")
                                    .setCity("Anytown")
                                    .setState("ST")
                                    .setPostalCode("12345")
                                    .setCountry("US"))
                            .mutateContactInfo(contactInfos -> contactInfos
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.EMAIL)
                                            .setValue("contact@acme.com"))
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.PHONE)
                                            .setValue("555-1234"))))
                    .put(PartyType.RECEIVER, party -> party
                            .setName("John Doe")
                            .mutateAddress(address -> address
                                    .setAddress1("456 Oak Ave")
                                    .setCity("Somewhere")
                                    .setState("CA")
                                    .setPostalCode("67890")
                                    .setCountry("US"))
                            .mutateContactInfo(contactInfos -> contactInfos
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.EMAIL)
                                            .setValue("john.doe@example.com"))
                                    .add(contactInfo -> contactInfo
                                            .setType(ContactInfoType.PHONE)
                                            .setValue("555-5678")))))
            .mutateParcels(parcels -> parcels
                    .add(parcel -> parcel
                            .setParcelNo("PARCEL001")
                            .setWeight(new BigDecimal("2.5"))
                            .setLength(new BigDecimal("10.0"))
                            .setWidth(new BigDecimal("8.0"))
                            .setHeight(new BigDecimal("6.0"))
                            .setDescription("Sample Package")
                            .setType(ParcelType.EXPRESS)
                            .setStatus(ParcelStatus.CREATED)))
            .mutateProformaInvoice(proformaInvoice -> proformaInvoice
                    .setInvoiceNo("INV001")
                    .setDescription("Sample Invoice")
                    .setTotalAmount(new BigDecimal("100.00"))
                    .setIssueDate(LocalDateTime.now()))
            .mutateSpecialInstructions(specialInstructions -> specialInstructions
                    .add("Handle with care")
                    .add("Fragile"))
            .setCreatedDate(LocalDateTime.now())
            .setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5))
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