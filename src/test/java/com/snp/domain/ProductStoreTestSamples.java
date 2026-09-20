package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductStoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductStore getProductStoreSample1() {
        return new ProductStore().id(1L).name("name1").title("title1");
    }

    public static ProductStore getProductStoreSample2() {
        return new ProductStore().id(2L).name("name2").title("title2");
    }

    public static ProductStore getProductStoreRandomSampleGenerator() {
        return new ProductStore().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).title(UUID.randomUUID().toString());
    }
}
