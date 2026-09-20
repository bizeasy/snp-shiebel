package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OtpAuthCriteriaTest {

    @Test
    void newOtpAuthCriteriaHasAllFiltersNullTest() {
        var otpAuthCriteria = new OtpAuthCriteria();
        assertThat(otpAuthCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void otpAuthCriteriaFluentMethodsCreatesFiltersTest() {
        var otpAuthCriteria = new OtpAuthCriteria();

        setAllFilters(otpAuthCriteria);

        assertThat(otpAuthCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void otpAuthCriteriaCopyCreatesNullFilterTest() {
        var otpAuthCriteria = new OtpAuthCriteria();
        var copy = otpAuthCriteria.copy();

        assertThat(otpAuthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(otpAuthCriteria)
        );
    }

    @Test
    void otpAuthCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var otpAuthCriteria = new OtpAuthCriteria();
        setAllFilters(otpAuthCriteria);

        var copy = otpAuthCriteria.copy();

        assertThat(otpAuthCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(otpAuthCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var otpAuthCriteria = new OtpAuthCriteria();

        assertThat(otpAuthCriteria).hasToString("OtpAuthCriteria{}");
    }

    private static void setAllFilters(OtpAuthCriteria otpAuthCriteria) {
        otpAuthCriteria.id();
        otpAuthCriteria.otp();
        otpAuthCriteria.otpSent();
        otpAuthCriteria.verificationSuccess();
        otpAuthCriteria.otpExpired();
        otpAuthCriteria.otpSentTime();
        otpAuthCriteria.sentCounter();
        otpAuthCriteria.failCounter();
        otpAuthCriteria.otpResetCounter();
        otpAuthCriteria.maxResend();
        otpAuthCriteria.maxReset();
        otpAuthCriteria.maxFailures();
        otpAuthCriteria.otpActiveTime();
        otpAuthCriteria.partyId();
        otpAuthCriteria.distinct();
    }

    private static Condition<OtpAuthCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOtp()) &&
                condition.apply(criteria.getOtpSent()) &&
                condition.apply(criteria.getVerificationSuccess()) &&
                condition.apply(criteria.getOtpExpired()) &&
                condition.apply(criteria.getOtpSentTime()) &&
                condition.apply(criteria.getSentCounter()) &&
                condition.apply(criteria.getFailCounter()) &&
                condition.apply(criteria.getOtpResetCounter()) &&
                condition.apply(criteria.getMaxResend()) &&
                condition.apply(criteria.getMaxReset()) &&
                condition.apply(criteria.getMaxFailures()) &&
                condition.apply(criteria.getOtpActiveTime()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OtpAuthCriteria> copyFiltersAre(OtpAuthCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOtp(), copy.getOtp()) &&
                condition.apply(criteria.getOtpSent(), copy.getOtpSent()) &&
                condition.apply(criteria.getVerificationSuccess(), copy.getVerificationSuccess()) &&
                condition.apply(criteria.getOtpExpired(), copy.getOtpExpired()) &&
                condition.apply(criteria.getOtpSentTime(), copy.getOtpSentTime()) &&
                condition.apply(criteria.getSentCounter(), copy.getSentCounter()) &&
                condition.apply(criteria.getFailCounter(), copy.getFailCounter()) &&
                condition.apply(criteria.getOtpResetCounter(), copy.getOtpResetCounter()) &&
                condition.apply(criteria.getMaxResend(), copy.getMaxResend()) &&
                condition.apply(criteria.getMaxReset(), copy.getMaxReset()) &&
                condition.apply(criteria.getMaxFailures(), copy.getMaxFailures()) &&
                condition.apply(criteria.getOtpActiveTime(), copy.getOtpActiveTime()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
