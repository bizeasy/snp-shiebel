package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductFeature getProductFeatureSample1() {
        return new ProductFeature().id(1L).name("name1").description("description1").sequenceNo(1).abbrev("abbrev1");
    }

    public static ProductFeature getProductFeatureSample2() {
        return new ProductFeature().id(2L).name("name2").description("description2").sequenceNo(2).abbrev("abbrev2");
    }

    public static ProductFeature getProductFeatureRandomSampleGenerator() {
        return new ProductFeature()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .sequenceNo(intCount.incrementAndGet())
            .abbrev(UUID.randomUUID().toString());
    }
}
