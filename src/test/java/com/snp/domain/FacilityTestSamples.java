package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Facility getFacilitySample1() {
        return new Facility().id(1L).name("name1").description("description1");
    }

    public static Facility getFacilitySample2() {
        return new Facility().id(2L).name("name2").description("description2");
    }

    public static Facility getFacilityRandomSampleGenerator() {
        return new Facility().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
