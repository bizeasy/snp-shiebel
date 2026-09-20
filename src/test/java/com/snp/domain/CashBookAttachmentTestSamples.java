package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CashBookAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CashBookAttachment getCashBookAttachmentSample1() {
        return new CashBookAttachment().id(1L).name("name1");
    }

    public static CashBookAttachment getCashBookAttachmentSample2() {
        return new CashBookAttachment().id(2L).name("name2");
    }

    public static CashBookAttachment getCashBookAttachmentRandomSampleGenerator() {
        return new CashBookAttachment().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
