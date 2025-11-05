package io.github.larsarv.jrmg.test.project.example.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.math.BigDecimal;

@GenerateMutator
public record InvoiceLineItem(
    String itemCode,
    String itemName,
    String description,
    BigDecimal taxAmount,
    BigDecimal totalAmount,
    int quantity,
    BigDecimal unitRate
) {
}
