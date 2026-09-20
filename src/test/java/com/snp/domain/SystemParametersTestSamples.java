package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemParametersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static SystemParameters getSystemParametersSample1() {
        return new SystemParameters().id(1L).key("key1").value("value1").description("description1").environment("environment1");
    }

    public static SystemParameters getSystemParametersSample2() {
        return new SystemParameters().id(2L).key("key2").value("value2").description("description2").environment("environment2");
    }

    public static SystemParameters getSystemParametersRandomSampleGenerator() {
        return new SystemParameters()
            .id(longCount.incrementAndGet())
            .key(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .environment(UUID.randomUUID().toString());
    }
}
