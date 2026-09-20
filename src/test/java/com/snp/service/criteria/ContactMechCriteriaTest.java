package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContactMechCriteriaTest {

    @Test
    void newContactMechCriteriaHasAllFiltersNullTest() {
        var contactMechCriteria = new ContactMechCriteria();
        assertThat(contactMechCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contactMechCriteriaFluentMethodsCreatesFiltersTest() {
        var contactMechCriteria = new ContactMechCriteria();

        setAllFilters(contactMechCriteria);

        assertThat(contactMechCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contactMechCriteriaCopyCreatesNullFilterTest() {
        var contactMechCriteria = new ContactMechCriteria();
        var copy = contactMechCriteria.copy();

        assertThat(contactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contactMechCriteria)
        );
    }

    @Test
    void contactMechCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contactMechCriteria = new ContactMechCriteria();
        setAllFilters(contactMechCriteria);

        var copy = contactMechCriteria.copy();

        assertThat(contactMechCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contactMechCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contactMechCriteria = new ContactMechCriteria();

        assertThat(contactMechCriteria).hasToString("ContactMechCriteria{}");
    }

    private static void setAllFilters(ContactMechCriteria contactMechCriteria) {
        contactMechCriteria.id();
        contactMechCriteria.infoString();
        contactMechCriteria.contactMechTypeId();
        contactMechCriteria.distinct();
    }

    private static Condition<ContactMechCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInfoString()) &&
                condition.apply(criteria.getContactMechTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContactMechCriteria> copyFiltersAre(ContactMechCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInfoString(), copy.getInfoString()) &&
                condition.apply(criteria.getContactMechTypeId(), copy.getContactMechTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
