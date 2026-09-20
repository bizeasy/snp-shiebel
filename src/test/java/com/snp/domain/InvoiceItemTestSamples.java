package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static InvoiceItem getInvoiceItemSample1() {
        return new InvoiceItem().id(1L).sequenceNo(1).attr1("attr11").attr2("attr21").attr3("attr31");
    }

    public static InvoiceItem getInvoiceItemSample2() {
        return new InvoiceItem().id(2L).sequenceNo(2).attr1("attr12").attr2("attr22").attr3("attr32");
    }

    public static InvoiceItem getInvoiceItemRandomSampleGenerator() {
        return new InvoiceItem()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .attr1(UUID.randomUUID().toString())
            .attr2(UUID.randomUUID().toString())
            .attr3(UUID.randomUUID().toString());
    }
}
