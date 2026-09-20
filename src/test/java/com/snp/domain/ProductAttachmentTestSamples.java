package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ProductAttachment getProductAttachmentSample1() {
        return new ProductAttachment().id(1L).name("name1");
    }

    public static ProductAttachment getProductAttachmentSample2() {
        return new ProductAttachment().id(2L).name("name2");
    }

    public static ProductAttachment getProductAttachmentRandomSampleGenerator() {
        return new ProductAttachment().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
