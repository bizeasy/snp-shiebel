package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFacilityLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFacilityLocation getProductFacilityLocationSample1() {
        return new ProductFacilityLocation().id(1L);
    }

    public static ProductFacilityLocation getProductFacilityLocationSample2() {
        return new ProductFacilityLocation().id(2L);
    }

    public static ProductFacilityLocation getProductFacilityLocationRandomSampleGenerator() {
        return new ProductFacilityLocation().id(longCount.incrementAndGet());
    }
}
