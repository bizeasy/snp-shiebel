package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductType getProductTypeSample1() {
        return new ProductType().id(1L).name("name1").description("description1");
    }

    public static ProductType getProductTypeSample2() {
        return new ProductType().id(2L).name("name2").description("description2");
    }

    public static ProductType getProductTypeRandomSampleGenerator() {
        return new ProductType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
