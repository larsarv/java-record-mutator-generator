package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@GenerateCtor
@GenerateMtor
public record Shipment(
        String shipmentNo,
        ShipmentStatus status,
        Map<PartyType, Party> parties,
        List<Parcel> parcels,
        ProformaInvoice proformaInvoice,
        List<String> specialInstructions,
        LocalDateTime createdDate,
        LocalDateTime estimatedDeliveryDate
) {
}