package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductCategoryTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductCategoryType getProductCategoryTypeSample1() {
        return new ProductCategoryType().id(1L).name("name1").description("description1");
    }

    public static ProductCategoryType getProductCategoryTypeSample2() {
        return new ProductCategoryType().id(2L).name("name2").description("description2");
    }

    public static ProductCategoryType getProductCategoryTypeRandomSampleGenerator() {
        return new ProductCategoryType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
