package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustRequestCategory getCustRequestCategorySample1() {
        return new CustRequestCategory().id(1L).name("name1").description("description1");
    }

    public static CustRequestCategory getCustRequestCategorySample2() {
        return new CustRequestCategory().id(2L).name("name2").description("description2");
    }

    public static CustRequestCategory getCustRequestCategoryRandomSampleGenerator() {
        return new CustRequestCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
