package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record Address(
        String address1,
        String address2,
        String city,
        String state,
        String postalCode,
        String country
) {
}