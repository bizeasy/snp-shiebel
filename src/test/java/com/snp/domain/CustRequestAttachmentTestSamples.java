package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustRequestAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CustRequestAttachment getCustRequestAttachmentSample1() {
        return new CustRequestAttachment().id(1L).name("name1");
    }

    public static CustRequestAttachment getCustRequestAttachmentSample2() {
        return new CustRequestAttachment().id(2L).name("name2");
    }

    public static CustRequestAttachment getCustRequestAttachmentRandomSampleGenerator() {
        return new CustRequestAttachment().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
