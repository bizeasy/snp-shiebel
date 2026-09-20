package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoleTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RoleType getRoleTypeSample1() {
        return new RoleType().id(1L).name("name1").description("description1");
    }

    public static RoleType getRoleTypeSample2() {
        return new RoleType().id(2L).name("name2").description("description2");
    }

    public static RoleType getRoleTypeRandomSampleGenerator() {
        return new RoleType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
