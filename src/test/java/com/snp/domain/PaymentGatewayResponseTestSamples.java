package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentGatewayResponseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PaymentGatewayResponse getPaymentGatewayResponseSample1() {
        return new PaymentGatewayResponse().id(1L).referenceNumber("referenceNumber1").gatewayMessage("gatewayMessage1");
    }

    public static PaymentGatewayResponse getPaymentGatewayResponseSample2() {
        return new PaymentGatewayResponse().id(2L).referenceNumber("referenceNumber2").gatewayMessage("gatewayMessage2");
    }

    public static PaymentGatewayResponse getPaymentGatewayResponseRandomSampleGenerator() {
        return new PaymentGatewayResponse()
            .id(longCount.incrementAndGet())
            .referenceNumber(UUID.randomUUID().toString())
            .gatewayMessage(UUID.randomUUID().toString());
    }
}
