package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustRequestItemAttachmentCriteriaTest {

    @Test
    void newCustRequestItemAttachmentCriteriaHasAllFiltersNullTest() {
        var custRequestItemAttachmentCriteria = new CustRequestItemAttachmentCriteria();
        assertThat(custRequestItemAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void custRequestItemAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var custRequestItemAttachmentCriteria = new CustRequestItemAttachmentCriteria();

        setAllFilters(custRequestItemAttachmentCriteria);

        assertThat(custRequestItemAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void custRequestItemAttachmentCriteriaCopyCreatesNullFilterTest() {
        var custRequestItemAttachmentCriteria = new CustRequestItemAttachmentCriteria();
        var copy = custRequestItemAttachmentCriteria.copy();

        assertThat(custRequestItemAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestItemAttachmentCriteria)
        );
    }

    @Test
    void custRequestItemAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var custRequestItemAttachmentCriteria = new CustRequestItemAttachmentCriteria();
        setAllFilters(custRequestItemAttachmentCriteria);

        var copy = custRequestItemAttachmentCriteria.copy();

        assertThat(custRequestItemAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(custRequestItemAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var custRequestItemAttachmentCriteria = new CustRequestItemAttachmentCriteria();

        assertThat(custRequestItemAttachmentCriteria).hasToString("CustRequestItemAttachmentCriteria{}");
    }

    private static void setAllFilters(CustRequestItemAttachmentCriteria custRequestItemAttachmentCriteria) {
        custRequestItemAttachmentCriteria.id();
        custRequestItemAttachmentCriteria.name();
        custRequestItemAttachmentCriteria.custRequestItemId();
        custRequestItemAttachmentCriteria.attachmentId();
        custRequestItemAttachmentCriteria.distinct();
    }

    private static Condition<CustRequestItemAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCustRequestItemId()) &&
                condition.apply(criteria.getAttachmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustRequestItemAttachmentCriteria> copyFiltersAre(
        CustRequestItemAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCustRequestItemId(), copy.getCustRequestItemId()) &&
                condition.apply(criteria.getAttachmentId(), copy.getAttachmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
