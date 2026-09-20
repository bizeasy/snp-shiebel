package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderAttachment getOrderAttachmentSample1() {
        return new OrderAttachment().id(1L).name("name1");
    }

    public static OrderAttachment getOrderAttachmentSample2() {
        return new OrderAttachment().id(2L).name("name2");
    }

    public static OrderAttachment getOrderAttachmentRandomSampleGenerator() {
        return new OrderAttachment().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
