package io.github.larsarv.jrmg.test.project;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@GenerateMutator
public record Invoice(
        long invoiceNumber,
        Instant createdAt,
        Instant updatedAt,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate paidOnDate,
        BigDecimal totalAmount,
        BigDecimal remainingAmount,
        String currency,
        InvoiceStatus invoiceStatus,
        Address billingAddress,
        List<InvoiceLineItem> items,
        List<Integer> test1
) {
}
