package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FacilityGroup getFacilityGroupSample1() {
        return new FacilityGroup().id(1L).name("name1").description("description1");
    }

    public static FacilityGroup getFacilityGroupSample2() {
        return new FacilityGroup().id(2L).name("name2").description("description2");
    }

    public static FacilityGroup getFacilityGroupRandomSampleGenerator() {
        return new FacilityGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
