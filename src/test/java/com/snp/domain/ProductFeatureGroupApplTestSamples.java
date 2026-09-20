package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureGroupApplTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductFeatureGroupAppl getProductFeatureGroupApplSample1() {
        return new ProductFeatureGroupAppl().id(1L).sequenceNo(1);
    }

    public static ProductFeatureGroupAppl getProductFeatureGroupApplSample2() {
        return new ProductFeatureGroupAppl().id(2L).sequenceNo(2);
    }

    public static ProductFeatureGroupAppl getProductFeatureGroupApplRandomSampleGenerator() {
        return new ProductFeatureGroupAppl().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
