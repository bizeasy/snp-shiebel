package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogueCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CatalogueCategory getCatalogueCategorySample1() {
        return new CatalogueCategory().id(1L).sequenceNo(1);
    }

    public static CatalogueCategory getCatalogueCategorySample2() {
        return new CatalogueCategory().id(2L).sequenceNo(2);
    }

    public static CatalogueCategory getCatalogueCategoryRandomSampleGenerator() {
        return new CatalogueCategory().id(longCount.incrementAndGet()).sequenceNo(intCount.incrementAndGet());
    }
}
