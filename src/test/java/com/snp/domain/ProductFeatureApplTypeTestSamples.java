package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureApplTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFeatureApplType getProductFeatureApplTypeSample1() {
        return new ProductFeatureApplType().id(1L).name("name1").description("description1");
    }

    public static ProductFeatureApplType getProductFeatureApplTypeSample2() {
        return new ProductFeatureApplType().id(2L).name("name2").description("description2");
    }

    public static ProductFeatureApplType getProductFeatureApplTypeRandomSampleGenerator() {
        return new ProductFeatureApplType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
