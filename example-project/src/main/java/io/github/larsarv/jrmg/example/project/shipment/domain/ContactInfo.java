package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

@GenerateCtor
@GenerateMtor
public record ContactInfo(
        ContactInfoType type,
        String value
) {
}
