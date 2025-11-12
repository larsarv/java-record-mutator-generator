package io.github.larsarv.jrmg.example.project.shipment.domain;

import io.github.larsarv.jrmg.api.GenerateMutator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GenerateMutator
public record ProformaInvoice(
        String invoiceNo,
        String description,
        Map<String, BigDecimal> lineItemPrices,
        List<String> lineItemDescriptions,
        Map<String, Integer> quantities,
        Set<String> taxCodes,
        BigDecimal totalAmount,
        LocalDateTime issueDate,
        Map<String, String> customFields
) {
}