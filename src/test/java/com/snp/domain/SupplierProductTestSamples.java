package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static SupplierProduct getSupplierProductSample1() {
        return new SupplierProduct().id(1L).supplierProductId("supplierProductId1").comments("comments1").leadTimeDays(1);
    }

    public static SupplierProduct getSupplierProductSample2() {
        return new SupplierProduct().id(2L).supplierProductId("supplierProductId2").comments("comments2").leadTimeDays(2);
    }

    public static SupplierProduct getSupplierProductRandomSampleGenerator() {
        return new SupplierProduct()
            .id(longCount.incrementAndGet())
            .supplierProductId(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString())
            .leadTimeDays(intCount.incrementAndGet());
    }
}
