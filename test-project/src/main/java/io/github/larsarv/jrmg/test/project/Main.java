package io.github.larsarv.jrmg.test.project;

public class Main {
    public static void main(String[] args) {

        Invoice invoice = new InvoiceMutator()
                .setCurrency("SEK")
                .mutateBillingAddress(a -> a
                        .setAddressLine1("addr1")
                        .setAddressLine2("addr2"))
                .mutateItems(list -> list
                        .mutate(0, invoiceLine -> invoiceLine
                                .setItemCode("asdf0")))
                .mutateItems(list -> list
                        .mutateAll((index, item) -> item
                                .setItemCode("asdf" + index)))
                .mutateTest1(list -> list
                        .set(0, 1)
                        .updateAll((index, item) -> item))
                .build();

    }
}