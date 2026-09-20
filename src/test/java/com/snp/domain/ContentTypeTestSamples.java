package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ContentType getContentTypeSample1() {
        return new ContentType().id(1L).name("name1");
    }

    public static ContentType getContentTypeSample2() {
        return new ContentType().id(2L).name("name2");
    }

    public static ContentType getContentTypeRandomSampleGenerator() {
        return new ContentType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
