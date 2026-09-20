package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentGatewayConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PaymentGatewayConfig getPaymentGatewayConfigSample1() {
        return new PaymentGatewayConfig().id(1L).name("name1").authUrl("authUrl1").releaseUrl("releaseUrl1").refundUrl("refundUrl1");
    }

    public static PaymentGatewayConfig getPaymentGatewayConfigSample2() {
        return new PaymentGatewayConfig().id(2L).name("name2").authUrl("authUrl2").releaseUrl("releaseUrl2").refundUrl("refundUrl2");
    }

    public static PaymentGatewayConfig getPaymentGatewayConfigRandomSampleGenerator() {
        return new PaymentGatewayConfig()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .authUrl(UUID.randomUUID().toString())
            .releaseUrl(UUID.randomUUID().toString())
            .refundUrl(UUID.randomUUID().toString());
    }
}
