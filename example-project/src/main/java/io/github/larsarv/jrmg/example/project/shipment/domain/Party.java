package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.util.List;
import java.util.Map;
import java.util.Set;

@GenerateMutator
public record Party(
        String name,
        Address address,
        Set<ContactInfo> contactInfo
) {
}