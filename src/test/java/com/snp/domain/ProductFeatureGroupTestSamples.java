package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFeatureGroup getProductFeatureGroupSample1() {
        return new ProductFeatureGroup().id(1L).name("name1").description("description1");
    }

    public static ProductFeatureGroup getProductFeatureGroupSample2() {
        return new ProductFeatureGroup().id(2L).name("name2").description("description2");
    }

    public static ProductFeatureGroup getProductFeatureGroupRandomSampleGenerator() {
        return new ProductFeatureGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
