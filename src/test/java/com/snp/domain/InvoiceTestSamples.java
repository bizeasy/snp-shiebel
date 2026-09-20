package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Invoice getInvoiceSample1() {
        return new Invoice()
            .id(1L)
            .referenceNumber("referenceNumber1")
            .invoiceMessage("invoiceMessage1")
            .externalId("externalId1")
            .projectName("projectName1")
            .remarks("remarks1");
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice()
            .id(2L)
            .referenceNumber("referenceNumber2")
            .invoiceMessage("invoiceMessage2")
            .externalId("externalId2")
            .projectName("projectName2")
            .remarks("remarks2");
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice()
            .id(longCount.incrementAndGet())
            .referenceNumber(UUID.randomUUID().toString())
            .invoiceMessage(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString())
            .projectName(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
