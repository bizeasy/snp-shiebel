package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductKeywordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductKeyword getProductKeywordSample1() {
        return new ProductKeyword().id(1L).keyword("keyword1").relevancyWeight(1);
    }

    public static ProductKeyword getProductKeywordSample2() {
        return new ProductKeyword().id(2L).keyword("keyword2").relevancyWeight(2);
    }

    public static ProductKeyword getProductKeywordRandomSampleGenerator() {
        return new ProductKeyword()
            .id(longCount.incrementAndGet())
            .keyword(UUID.randomUUID().toString())
            .relevancyWeight(intCount.incrementAndGet());
    }
}
