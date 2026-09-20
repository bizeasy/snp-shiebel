package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GeoAssocTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static GeoAssocType getGeoAssocTypeSample1() {
        return new GeoAssocType().id(1L).name("name1");
    }

    public static GeoAssocType getGeoAssocTypeSample2() {
        return new GeoAssocType().id(2L).name("name2");
    }

    public static GeoAssocType getGeoAssocTypeRandomSampleGenerator() {
        return new GeoAssocType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
