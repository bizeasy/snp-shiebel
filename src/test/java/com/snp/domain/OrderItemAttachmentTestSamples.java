package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderItemAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static OrderItemAttachment getOrderItemAttachmentSample1() {
        return new OrderItemAttachment().id(1L).name("name1");
    }

    public static OrderItemAttachment getOrderItemAttachmentSample2() {
        return new OrderItemAttachment().id(2L).name("name2");
    }

    public static OrderItemAttachment getOrderItemAttachmentRandomSampleGenerator() {
        return new OrderItemAttachment().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
