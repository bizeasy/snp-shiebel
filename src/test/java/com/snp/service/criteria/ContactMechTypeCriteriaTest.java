package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContactMechTypeCriteriaTest {

    @Test
    void newContactMechTypeCriteriaHasAllFiltersNullTest() {
        var contactMechTypeCriteria = new ContactMechTypeCriteria();
        assertThat(contactMechTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contactMechTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var contactMechTypeCriteria = new ContactMechTypeCriteria();

        setAllFilters(contactMechTypeCriteria);

        assertThat(contactMechTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contactMechTypeCriteriaCopyCreatesNullFilterTest() {
        var contactMechTypeCriteria = new ContactMechTypeCriteria();
        var copy = contactMechTypeCriteria.copy();

        assertThat(contactMechTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contactMechTypeCriteria)
        );
    }

    @Test
    void contactMechTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contactMechTypeCriteria = new ContactMechTypeCriteria();
        setAllFilters(contactMechTypeCriteria);

        var copy = contactMechTypeCriteria.copy();

        assertThat(contactMechTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contactMechTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contactMechTypeCriteria = new ContactMechTypeCriteria();

        assertThat(contactMechTypeCriteria).hasToString("ContactMechTypeCriteria{}");
    }

    private static void setAllFilters(ContactMechTypeCriteria contactMechTypeCriteria) {
        contactMechTypeCriteria.id();
        contactMechTypeCriteria.name();
        contactMechTypeCriteria.description();
        contactMechTypeCriteria.distinct();
    }

    private static Condition<ContactMechTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContactMechTypeCriteria> copyFiltersAre(
        ContactMechTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
