package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustRequestType getCustRequestTypeSample1() {
        return new CustRequestType().id(1L).name("name1").description("description1");
    }

    public static CustRequestType getCustRequestTypeSample2() {
        return new CustRequestType().id(2L).name("name2").description("description2");
    }

    public static CustRequestType getCustRequestTypeRandomSampleGenerator() {
        return new CustRequestType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
