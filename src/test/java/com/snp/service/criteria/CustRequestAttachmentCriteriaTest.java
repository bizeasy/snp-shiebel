package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestAttachmentCriteriaTest {

    @Test
    void newCustRequestAttachmentCriteriaHasAllFiltersNullTest() {
        var custRequestAttachmentCriteria = new CustRequestAttachmentCriteria();
        assertThat(custRequestAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestAttachmentCriteria = new CustRequestAttachmentCriteria();

        setAllFilters(custRequestAttachmentCriteria);

        assertThat(custRequestAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestAttachmentCriteriaCopyCreatesNullFilterTest() {
        var custRequestAttachmentCriteria = new CustRequestAttachmentCriteria();
        var copy = custRequestAttachmentCriteria.copy();

        assertThat(custRequestAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestAttachmentCriteria)
        );
    }

    @Test
    void custRequestAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestAttachmentCriteria = new CustRequestAttachmentCriteria();
        setAllFilters(custRequestAttachmentCriteria);

        var copy = custRequestAttachmentCriteria.copy();

        assertThat(custRequestAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestAttachmentCriteria = new CustRequestAttachmentCriteria();

        assertThat(custRequestAttachmentCriteria).hasToString("CustRequestAttachmentCriteria{}");
    }

    private static void setAllFilters(CustRequestAttachmentCriteria custRequestAttachmentCriteria) {
        custRequestAttachmentCriteria.id();
        custRequestAttachmentCriteria.name();
        custRequestAttachmentCriteria.custRequestId();
        custRequestAttachmentCriteria.attachmentId();
        custRequestAttachmentCriteria.distinct();
    }

    private static Condition<CustRequestAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCustRequestId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestAttachmentCriteria> copyFiltersAre(
        CustRequestAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCustRequestId(), copy.getCustRequestId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
