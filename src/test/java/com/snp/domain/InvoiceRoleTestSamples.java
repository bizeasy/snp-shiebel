package com.snp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static InvoiceRole getInvoiceRoleSample1() {
        return new InvoiceRole().id(1L);
    }

    public static InvoiceRole getInvoiceRoleSample2() {
        return new InvoiceRole().id(2L);
    }

    public static InvoiceRole getInvoiceRoleRandomSampleGenerator() {
        return new InvoiceRole().id(longCount.incrementAndGet());
    }
}
