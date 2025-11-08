package io.github.larsarv.jrmg.example.project.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

@GenerateMutator
public record Address(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String zipCode
) {
}
