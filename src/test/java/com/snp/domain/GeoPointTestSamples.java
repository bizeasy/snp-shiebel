package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GeoPointTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static GeoPoint getGeoPointSample1() {
        return new GeoPoint().id(1L);
    }

    public static GeoPoint getGeoPointSample2() {
        return new GeoPoint().id(2L);
    }

    public static GeoPoint getGeoPointRandomSampleGenerator() {
        return new GeoPoint().id(longCount.incrementAndGet());
    }
}
