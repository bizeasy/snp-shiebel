package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFacilityUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFacilityUser getProductFacilityUserSample1() {
        return new ProductFacilityUser().id(1L);
    }

    public static ProductFacilityUser getProductFacilityUserSample2() {
        return new ProductFacilityUser().id(2L);
    }

    public static ProductFacilityUser getProductFacilityUserRandomSampleGenerator() {
        return new ProductFacilityUser().id(longCount.incrementAndGet());
    }
}
