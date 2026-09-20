package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactMechTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ContactMech getContactMechSample1() {
        return new ContactMech().id(1L).infoString("infoString1");
    }

    public static ContactMech getContactMechSample2() {
        return new ContactMech().id(2L).infoString("infoString2");
    }

    public static ContactMech getContactMechRandomSampleGenerator() {
        return new ContactMech().id(longCount.incrementAndGet()).infoString(UUID.randomUUID().toString());
    }
}
