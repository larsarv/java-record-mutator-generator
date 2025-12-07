package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

@GenerateCtor
@GenerateMtor
public record Address(
        String address1,
        String address2,
        String city,
        String state,
        String postalCode,
        String country
) {
}