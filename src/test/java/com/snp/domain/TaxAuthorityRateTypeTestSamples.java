package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaxAuthorityRateTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TaxAuthorityRateType getTaxAuthorityRateTypeSample1() {
        return new TaxAuthorityRateType().id(1L).name("name1").description("description1");
    }

    public static TaxAuthorityRateType getTaxAuthorityRateTypeSample2() {
        return new TaxAuthorityRateType().id(2L).name("name2").description("description2");
    }

    public static TaxAuthorityRateType getTaxAuthorityRateTypeRandomSampleGenerator() {
        return new TaxAuthorityRateType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
