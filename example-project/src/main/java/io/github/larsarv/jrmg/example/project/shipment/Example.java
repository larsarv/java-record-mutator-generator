package io.github.larsarv.jrmg.example.project.shipment;

import io.github.larsarv.jrmg.example.project.shipment.domain.*;
import io.github.larsarv.jrmg.api.NestedListMutateFunction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class Example {
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
}
