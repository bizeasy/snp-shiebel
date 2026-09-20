package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GeoTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static GeoType getGeoTypeSample1() {
        return new GeoType().id(1L).name("name1");
    }

    public static GeoType getGeoTypeSample2() {
        return new GeoType().id(2L).name("name2");
    }

    public static GeoType getGeoTypeRandomSampleGenerator() {
        return new GeoType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
