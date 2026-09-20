package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductPriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductPrice getProductPriceSample1() {
        return new ProductPrice().id(1L);
    }

    public static ProductPrice getProductPriceSample2() {
        return new ProductPrice().id(2L);
    }

    public static ProductPrice getProductPriceRandomSampleGenerator() {
        return new ProductPrice().id(longCount.incrementAndGet());
    }
}
