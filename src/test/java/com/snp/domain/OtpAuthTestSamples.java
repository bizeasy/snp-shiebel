package com.snp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OtpAuthTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static OtpAuth getOtpAuthSample1() {
        return new OtpAuth()
            .id(1L)
            .otp("otp1")
            .sentCounter(1)
            .failCounter(1)
            .otpResetCounter(1)
            .maxResend(1)
            .maxReset(1)
            .maxFailures(1)
            .otpActiveTime(1);
    }

    public static OtpAuth getOtpAuthSample2() {
        return new OtpAuth()
            .id(2L)
            .otp("otp2")
            .sentCounter(2)
            .failCounter(2)
            .otpResetCounter(2)
            .maxResend(2)
            .maxReset(2)
            .maxFailures(2)
            .otpActiveTime(2);
    }

    public static OtpAuth getOtpAuthRandomSampleGenerator() {
        return new OtpAuth()
            .id(longCount.incrementAndGet())
            .otp(UUID.randomUUID().toString())
            .sentCounter(intCount.incrementAndGet())
            .failCounter(intCount.incrementAndGet())
            .otpResetCounter(intCount.incrementAndGet())
            .maxResend(intCount.incrementAndGet())
            .maxReset(intCount.incrementAndGet())
            .maxFailures(intCount.incrementAndGet())
            .otpActiveTime(intCount.incrementAndGet());
    }
}
