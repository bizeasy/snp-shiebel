package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Payment getPaymentSample1() {
        return new Payment()
            .id(1L)
            .paymentRefNumber("paymentRefNumber1")
            .paymentStatus("paymentStatus1")
            .mihpayId("mihpayId1")
            .txnId("txnId1")
            .email("email1")
            .phone("phone1")
            .productInfo("productInfo1");
    }

    public static Payment getPaymentSample2() {
        return new Payment()
            .id(2L)
            .paymentRefNumber("paymentRefNumber2")
            .paymentStatus("paymentStatus2")
            .mihpayId("mihpayId2")
            .txnId("txnId2")
            .email("email2")
            .phone("phone2")
            .productInfo("productInfo2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .paymentRefNumber(UUID.randomUUID().toString())
            .paymentStatus(UUID.randomUUID().toString())
            .mihpayId(UUID.randomUUID().toString())
            .txnId(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .productInfo(UUID.randomUUID().toString());
    }
}
