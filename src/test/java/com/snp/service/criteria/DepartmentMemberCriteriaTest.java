package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DepartmentMemberCriteriaTest {

    @Test
    void newDepartmentMemberCriteriaHasAllFiltersNullTest() {
        var departmentMemberCriteria = new DepartmentMemberCriteria();
        assertThat(departmentMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void departmentMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var departmentMemberCriteria = new DepartmentMemberCriteria();

        setAllFilters(departmentMemberCriteria);

        assertThat(departmentMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void departmentMemberCriteriaCopyCreatesNullFilterTest() {
        var departmentMemberCriteria = new DepartmentMemberCriteria();
        var copy = departmentMemberCriteria.copy();

        assertThat(departmentMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentMemberCriteria)
        );
    }

    @Test
    void departmentMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var departmentMemberCriteria = new DepartmentMemberCriteria();
        setAllFilters(departmentMemberCriteria);

        var copy = departmentMemberCriteria.copy();

        assertThat(departmentMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var departmentMemberCriteria = new DepartmentMemberCriteria();

        assertThat(departmentMemberCriteria).hasToString("DepartmentMemberCriteria{}");
    }

    private static void setAllFilters(DepartmentMemberCriteria departmentMemberCriteria) {
        departmentMemberCriteria.id();
        departmentMemberCriteria.fromDate();
        departmentMemberCriteria.thruDate();
        departmentMemberCriteria.isPrimary();
        departmentMemberCriteria.departmentId();
        departmentMemberCriteria.partyId();
        departmentMemberCriteria.roleTypeId();
        departmentMemberCriteria.distinct();
    }

    private static Condition<DepartmentMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getThruDate()) &&
                condition.apply(criteria.getIsPrimary()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DepartmentMemberCriteria> copyFiltersAre(
        DepartmentMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getThruDate(), copy.getThruDate()) &&
                condition.apply(criteria.getIsPrimary(), copy.getIsPrimary()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getPartyId(), copy.getPartyId()) &&
                condition.apply(criteria.getRoleTypeId(), copy.getRoleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
