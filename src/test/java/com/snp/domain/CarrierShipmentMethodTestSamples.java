package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarrierShipmentMethodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CarrierShipmentMethod getCarrierShipmentMethodSample1() {
        return new CarrierShipmentMethod().id(1L).sequenceNo(1).carrierServiceCode("carrierServiceCode1");
    }

    public static CarrierShipmentMethod getCarrierShipmentMethodSample2() {
        return new CarrierShipmentMethod().id(2L).sequenceNo(2).carrierServiceCode("carrierServiceCode2");
    }

    public static CarrierShipmentMethod getCarrierShipmentMethodRandomSampleGenerator() {
        return new CarrierShipmentMethod()
            .id(longCount.incrementAndGet())
            .sequenceNo(intCount.incrementAndGet())
            .carrierServiceCode(UUID.randomUUID().toString());
    }
}
