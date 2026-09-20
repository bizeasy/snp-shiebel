package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityContactMechTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FacilityContactMech getFacilityContactMechSample1() {
        return new FacilityContactMech().id(1L);
    }

    public static FacilityContactMech getFacilityContactMechSample2() {
        return new FacilityContactMech().id(2L);
    }

    public static FacilityContactMech getFacilityContactMechRandomSampleGenerator() {
        return new FacilityContactMech().id(longCount.incrementAndGet());
    }
}
