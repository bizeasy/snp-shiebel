package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaxAuthorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TaxAuthority getTaxAuthoritySample1() {
        return new TaxAuthority().id(1L).name("name1");
    }

    public static TaxAuthority getTaxAuthoritySample2() {
        return new TaxAuthority().id(2L).name("name2");
    }

    public static TaxAuthority getTaxAuthorityRandomSampleGenerator() {
        return new TaxAuthority().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
