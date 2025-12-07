package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateCtor;
import io.github.larsarv.jrmg.api.GenerateMtor;

import java.util.Set;

@GenerateCtor
@GenerateMtor
public record Party(
        String name,
        Address address,
        Set<ContactInfo> contactInfo
) {
}