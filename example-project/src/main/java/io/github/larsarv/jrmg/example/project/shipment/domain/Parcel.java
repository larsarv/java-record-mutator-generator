package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import java.math.BigDecimal;
import java.util.List;

@GenerateCtor
@GenerateMtor
public record Parcel(
        String parcelNo,
        BigDecimal weight,
        BigDecimal length,
        BigDecimal width,
        BigDecimal height,
        String description,
        List<String> contents,
        ParcelType type,
        ParcelStatus status
) {
}