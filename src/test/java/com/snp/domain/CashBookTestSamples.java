package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CashBookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CashBook getCashBookSample1() {
        return new CashBook()
            .id(1L)
            .description("description1")
            .vendorCustomerName("vendorCustomerName1")
            .invoiceNo("invoiceNo1")
            .purpose("purpose1")
            .remarks("remarks1")
            .project("project1")
            .usageUnits("usageUnits1");
    }

    public static CashBook getCashBookSample2() {
        return new CashBook()
            .id(2L)
            .description("description2")
            .vendorCustomerName("vendorCustomerName2")
            .invoiceNo("invoiceNo2")
            .purpose("purpose2")
            .remarks("remarks2")
            .project("project2")
            .usageUnits("usageUnits2");
    }

    public static CashBook getCashBookRandomSampleGenerator() {
        return new CashBook()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .vendorCustomerName(UUID.randomUUID().toString())
            .invoiceNo(UUID.randomUUID().toString())
            .purpose(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString())
            .project(UUID.randomUUID().toString())
            .usageUnits(UUID.randomUUID().toString());
    }
}
