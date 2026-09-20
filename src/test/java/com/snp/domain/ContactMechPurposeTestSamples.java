package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactMechPurposeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ContactMechPurpose getContactMechPurposeSample1() {
        return new ContactMechPurpose().id(1L).name("name1").description("description1");
    }

    public static ContactMechPurpose getContactMechPurposeSample2() {
        return new ContactMechPurpose().id(2L).name("name2").description("description2");
    }

    public static ContactMechPurpose getContactMechPurposeRandomSampleGenerator() {
        return new ContactMechPurpose()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
