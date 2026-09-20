package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceItemTypeMapTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InvoiceItemTypeMap getInvoiceItemTypeMapSample1() {
        return new InvoiceItemTypeMap().id(1L).invoiceItemMapKey("invoiceItemMapKey1");
    }

    public static InvoiceItemTypeMap getInvoiceItemTypeMapSample2() {
        return new InvoiceItemTypeMap().id(2L).invoiceItemMapKey("invoiceItemMapKey2");
    }

    public static InvoiceItemTypeMap getInvoiceItemTypeMapRandomSampleGenerator() {
        return new InvoiceItemTypeMap().id(longCount.incrementAndGet()).invoiceItemMapKey(UUID.randomUUID().toString());
    }
}
