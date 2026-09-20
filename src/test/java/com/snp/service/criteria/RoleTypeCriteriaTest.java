package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RoleTypeCriteriaTest {

    @Test
    void newRoleTypeCriteriaHasAllFiltersNullTest() {
        var roleTypeCriteria = new RoleTypeCriteria();
        assertThat(roleTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void roleTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var roleTypeCriteria = new RoleTypeCriteria();

        setAllFilters(roleTypeCriteria);

        assertThat(roleTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void roleTypeCriteriaCopyCreatesNullFilterTest() {
        var roleTypeCriteria = new RoleTypeCriteria();
        var copy = roleTypeCriteria.copy();

        assertThat(roleTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(roleTypeCriteria)
        );
    }

    @Test
    void roleTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var roleTypeCriteria = new RoleTypeCriteria();
        setAllFilters(roleTypeCriteria);

        var copy = roleTypeCriteria.copy();

        assertThat(roleTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(roleTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var roleTypeCriteria = new RoleTypeCriteria();

        assertThat(roleTypeCriteria).hasToString("RoleTypeCriteria{}");
    }

    private static void setAllFilters(RoleTypeCriteria roleTypeCriteria) {
        roleTypeCriteria.id();
        roleTypeCriteria.name();
        roleTypeCriteria.description();
        roleTypeCriteria.distinct();
    }

    private static Condition<RoleTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RoleTypeCriteria> copyFiltersAre(RoleTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
