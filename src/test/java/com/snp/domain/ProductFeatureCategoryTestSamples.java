package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFeatureCategory getProductFeatureCategorySample1() {
        return new ProductFeatureCategory().id(1L).name("name1").description("description1");
    }

    public static ProductFeatureCategory getProductFeatureCategorySample2() {
        return new ProductFeatureCategory().id(2L).name("name2").description("description2");
    }

    public static ProductFeatureCategory getProductFeatureCategoryRandomSampleGenerator() {
        return new ProductFeatureCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
