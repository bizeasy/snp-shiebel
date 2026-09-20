package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFeatureType getProductFeatureTypeSample1() {
        return new ProductFeatureType().id(1L).name("name1").description("description1");
    }

    public static ProductFeatureType getProductFeatureTypeSample2() {
        return new ProductFeatureType().id(2L).name("name2").description("description2");
    }

    public static ProductFeatureType getProductFeatureTypeRandomSampleGenerator() {
        return new ProductFeatureType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
