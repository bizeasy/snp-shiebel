package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFacilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductFacility getProductFacilitySample1() {
        return new ProductFacility().id(1L).daysToShip(1);
    }

    public static ProductFacility getProductFacilitySample2() {
        return new ProductFacility().id(2L).daysToShip(2);
    }

    public static ProductFacility getProductFacilityRandomSampleGenerator() {
        return new ProductFacility().id(longCount.incrementAndGet()).daysToShip(intCount.incrementAndGet());
    }
}
