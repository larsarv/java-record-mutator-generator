package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

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