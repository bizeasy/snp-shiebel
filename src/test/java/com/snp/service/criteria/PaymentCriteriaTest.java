package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentCriteriaTest {

    @Test
    void newPaymentCriteriaHasAllFiltersNullTest() {
        var paymentCriteria = new PaymentCriteria();
        assertThat(paymentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentCriteria = new PaymentCriteria();

        setAllFilters(paymentCriteria);

        assertThat(paymentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentCriteriaCopyCreatesNullFilterTest() {
        var paymentCriteria = new PaymentCriteria();
        var copy = paymentCriteria.copy();

        assertThat(paymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentCriteria)
        );
    }

    @Test
    void paymentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentCriteria = new PaymentCriteria();
        setAllFilters(paymentCriteria);

        var copy = paymentCriteria.copy();

        assertThat(paymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentCriteria = new PaymentCriteria();

        assertThat(paymentCriteria).hasToString("PaymentCriteria{}");
    }

    private static void setAllFilters(PaymentCriteria paymentCriteria) {
        paymentCriteria.id();
        paymentCriteria.paymentRefNumber();
        paymentCriteria.effectiveDate();
        paymentCriteria.paymentDate();
        paymentCriteria.amount();
        paymentCriteria.actualAmount();
        paymentCriteria.paymentStatus();
        paymentCriteria.mihpayId();
        paymentCriteria.txnId();
        paymentCriteria.email();
        paymentCriteria.phone();
        paymentCriteria.productInfo();
        paymentCriteria.paymentTypeId();
        paymentCriteria.paymentMethodTypeId();
        paymentCriteria.paymentMethodId();
        paymentCriteria.paymentGatewayResponseId();
        paymentCriteria.statusId();
        paymentCriteria.fromPartyId();
        paymentCriteria.toPartyId();
        paymentCriteria.roleTypeId();
        paymentCriteria.currencyUomId();
        paymentCriteria.distinct();
    }

    private static Condition<PaymentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPaymentRefNumber()) &&
                condition.apply(criteria.getEffectiveDate()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getActualAmount()) &&
                condition.apply(criteria.getPaymentStatus()) &&
                condition.apply(criteria.getMihpayId()) &&
                condition.apply(criteria.getTxnId()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getProductInfo()) &&
                condition.apply(criteria.getPaymentTypeId()) &&
                condition.apply(criteria.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getPaymentMethodId()) &&
                condition.apply(criteria.getPaymentGatewayResponseId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentCriteria> copyFiltersAre(PaymentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPaymentRefNumber(), copy.getPaymentRefNumber()) &&
                condition.apply(criteria.getEffectiveDate(), copy.getEffectiveDate()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getActualAmount(), copy.getActualAmount()) &&
                condition.apply(criteria.getPaymentStatus(), copy.getPaymentStatus()) &&
                condition.apply(criteria.getMihpayId(), copy.getMihpayId()) &&
                condition.apply(criteria.getTxnId(), copy.getTxnId()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getProductInfo(), copy.getProductInfo()) &&
                condition.apply(criteria.getPaymentTypeId(), copy.getPaymentTypeId()) &&
                condition.apply(criteria.getPaymentMethodTypeId(), copy.getPaymentMethodTypeId()) &&
                condition.apply(criteria.getPaymentMethodId(), copy.getPaymentMethodId()) &&
                condition.apply(criteria.getPaymentGatewayResponseId(), copy.getPaymentGatewayResponseId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getFromPartyId(), copy.getFromPartyId()) &&
                condition.apply(criteria.getToPartyId(), copy.getToPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
