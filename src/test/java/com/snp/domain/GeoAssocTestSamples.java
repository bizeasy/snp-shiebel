package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GeoAssocTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static GeoAssoc getGeoAssocSample1() {
        return new GeoAssoc().id(1L);
    }

    public static GeoAssoc getGeoAssocSample2() {
        return new GeoAssoc().id(2L);
    }

    public static GeoAssoc getGeoAssocRandomSampleGenerator() {
        return new GeoAssoc().id(longCount.incrementAndGet());
    }
}
