package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaxSlabTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TaxSlab getTaxSlabSample1() {
        return new TaxSlab().id(1L).name("name1");
    }

    public static TaxSlab getTaxSlabSample2() {
        return new TaxSlab().id(2L).name("name2");
    }

    public static TaxSlab getTaxSlabRandomSampleGenerator() {
        return new TaxSlab().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
