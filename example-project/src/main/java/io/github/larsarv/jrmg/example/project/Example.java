package io.github.larsarv.jrmg.example.project;

import io.github.larsarv.jrmg.example.project.domain.Invoice;
import io.github.larsarv.jrmg.example.project.domain.InvoiceMutator;

public class Example {
    public static Invoice mutate(Invoice invoice) {
        return InvoiceMutator.mutator(invoice)
                .setCurrency("SEK")
                .mutateBillingAddress(a -> a
                        .setAddressLine1("address 1")
                        .setAddressLine2("address 2"))
                .mutateItems(list -> list
                        .mutate(0, invoiceLine -> invoiceLine
                                .setItemCode("1000")))
                .mutateItems(list -> list
                        .mutateAll((index, item) -> item
                                .setItemCode("100" + index)))
                .build();

    }
}