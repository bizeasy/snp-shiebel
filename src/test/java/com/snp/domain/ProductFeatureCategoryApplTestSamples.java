package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductFeatureCategoryApplTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductFeatureCategoryAppl getProductFeatureCategoryApplSample1() {
        return new ProductFeatureCategoryAppl().id(1L);
    }

    public static ProductFeatureCategoryAppl getProductFeatureCategoryApplSample2() {
        return new ProductFeatureCategoryAppl().id(2L);
    }

    public static ProductFeatureCategoryAppl getProductFeatureCategoryApplRandomSampleGenerator() {
        return new ProductFeatureCategoryAppl().id(longCount.incrementAndGet());
    }
}
