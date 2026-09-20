package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceItemTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InvoiceItemType getInvoiceItemTypeSample1() {
        return new InvoiceItemType().id(1L).name("name1").description("description1");
    }

    public static InvoiceItemType getInvoiceItemTypeSample2() {
        return new InvoiceItemType().id(2L).name("name2").description("description2");
    }

    public static InvoiceItemType getInvoiceItemTypeRandomSampleGenerator() {
        return new InvoiceItemType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
