package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record ContactInfo(
        ContactInfoType type,
        String value
) {
}
