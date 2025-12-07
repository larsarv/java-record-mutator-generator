package io.github.larsarv.jrmg.example.project.shipment;

import io.github.larsarv.jrmg.example.project.shipment.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Example {
    public Shipment updateParcelStatus(Shipment shipment, String parcelNo, ParcelStatus parcelStatus) {
        return ShipmentMtor.mutator(shipment)
                .mutateParcels(parcels -> parcels
                        .findFirstAndMutate(parcel -> parcelNo.equals(parcel.parcelNo()), parcel -> parcel
                                .setStatus(parcelStatus)))
                .build();
    }

    public Shipment updateShipmentWithNewParcel(Shipment originalShipment, Parcel parcel) {
        return ShipmentMtor.mutator(originalShipment)
                .mutateParcels(parcels -> parcels
                        .add(ParcelMtor.mutator(parcel)
                                .setStatus(ParcelStatus.CREATED)
                                .build()))
                .build();
    }

    public Shipment createShipmentTestData() {
        return ShipmentCtor.constructor()
                .setShipmentNo("SHP001")
                .setStatus(ShipmentStatus.CREATED)
                .constructParties(parties -> parties
                        .put(PartyType.SENDER, party -> party
                                .setName("Acme Corp")
                                .constructAddress(address -> address
                                        .setAddress1("123 Main St")
                                        .setAddress2(null)
                                        .setCity("Anytown")
                                        .setState("ST")
                                        .setPostalCode("12345")
                                        .setCountry("US"))
                                .constructContactInfo(contactInfos -> contactInfos
                                        .add(contactInfo -> contactInfo
                                                .setType(ContactInfoType.EMAIL)
                                                .setValue("contact@acme.com"))
                                        .add(contactInfo -> contactInfo
                                                .setType(ContactInfoType.PHONE)
                                                .setValue("555-1234"))))
                        .put(PartyType.RECEIVER, party -> party
                                .setName("John Doe")
                                .constructAddress(address -> address
                                        .setAddress1("456 Oak Ave")
                                        .setAddress2(null)
                                        .setCity("Somewhere")
                                        .setState("CA")
                                        .setPostalCode("67890")
                                        .setCountry("US"))
                                .constructContactInfo(contactInfos -> contactInfos
                                        .add(contactInfo -> contactInfo
                                                .setType(ContactInfoType.EMAIL)
                                                .setValue("john.doe@example.com"))
                                        .add(contactInfo -> contactInfo
                                                .setType(ContactInfoType.PHONE)
                                                .setValue("555-5678")))))
                .constructParcels(parcels -> parcels
                        .add(parcel -> parcel
                                .setParcelNo("PARCEL001")
                                .setWeight(new BigDecimal("2.5"))
                                .setLength(new BigDecimal("10.0"))
                                .setWidth(new BigDecimal("8.0"))
                                .setHeight(new BigDecimal("6.0"))
                                .setDescription("Sample Package")
                                .setContents(List.of())
                                .setType(ParcelType.EXPRESS)
                                .setStatus(ParcelStatus.CREATED)))
                .constructProformaInvoice(proformaInvoice -> proformaInvoice
                        .setInvoiceNo("INV001")
                        .setDescription("Sample Invoice")
                        .constructLineItemPrices(prices -> prices)
                        .constructLineItemDescriptions(lineItemDescriptions -> lineItemDescriptions)
                        .constructQuantities(quantities -> quantities
                                .put("test", 10))
                        .constructTaxCodes(taxCodes -> taxCodes)
                        .setTotalAmount(new BigDecimal("100.00"))
                        .setIssueDate(LocalDateTime.now())
                        .constructCustomFields(customFields -> customFields))
                .constructSpecialInstructions(specialInstructions -> specialInstructions
                        .add("Handle with care")
                        .add("Fragile"))
                .setCreatedDate(LocalDateTime.now())
                .setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5))
                .build();
    }
}
