package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductAssocTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductAssoc getProductAssocSample1() {
        return new ProductAssoc().id(1L).sequenceNo(1);
    }

    public static ProductAssoc getProductAssocSample2() {
        return new ProductAssoc().id(2L).sequenceNo(2);
    }

    public static ProductAssoc getProductAssocRandomSampleGenerator() {
        return new ProductAssoc().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
