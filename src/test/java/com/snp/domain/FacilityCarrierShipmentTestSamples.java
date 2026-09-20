package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityCarrierShipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static FacilityCarrierShipment getFacilityCarrierShipmentSample1() {
        return new FacilityCarrierShipment().id(1L);
    }

    public static FacilityCarrierShipment getFacilityCarrierShipmentSample2() {
        return new FacilityCarrierShipment().id(2L);
    }

    public static FacilityCarrierShipment getFacilityCarrierShipmentRandomSampleGenerator() {
        return new FacilityCarrierShipment().id(longCount.incrementAndGet());
    }
}
