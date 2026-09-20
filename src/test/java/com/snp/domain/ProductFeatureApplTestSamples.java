package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureApplTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductFeatureAppl getProductFeatureApplSample1() {
        return new ProductFeatureAppl().id(1L).sequenceNo(1);
    }

    public static ProductFeatureAppl getProductFeatureApplSample2() {
        return new ProductFeatureAppl().id(2L).sequenceNo(2);
    }

    public static ProductFeatureAppl getProductFeatureApplRandomSampleGenerator() {
        return new ProductFeatureAppl().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
