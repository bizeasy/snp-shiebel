package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LotCriteriaTest {

    @Test
    void newLotCriteriaHasAllFiltersNullTest() {
        var lotCriteria = new LotCriteria();
        assertThat(lotCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void lotCriteriaFluentMethodsCreatesFiltersTest() {
        var lotCriteria = new LotCriteria();

        setAllFilters(lotCriteria);

        assertThat(lotCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void lotCriteriaCopyCreatesNullFilterTest() {
        var lotCriteria = new LotCriteria();
        var copy = lotCriteria.copy();

        assertThat(lotCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(lotCriteria)
        );
    }

    @Test
    void lotCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var lotCriteria = new LotCriteria();
        setAllFilters(lotCriteria);

        var copy = lotCriteria.copy();

        assertThat(lotCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(lotCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var lotCriteria = new LotCriteria();

        assertThat(lotCriteria).hasToString("LotCriteria{}");
    }

    private static void setAllFilters(LotCriteria lotCriteria) {
        lotCriteria.id();
        lotCriteria.creationDate();
        lotCriteria.quantity();
        lotCriteria.expirationDate();
        lotCriteria.retestDate();
        lotCriteria.distinct();
    }

    private static Condition<LotCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreationDate()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getExpirationDate()) &&
                condition.apply(criteria.getRetestDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LotCriteria> copyFiltersAre(LotCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreationDate(), copy.getCreationDate()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getExpirationDate(), copy.getExpirationDate()) &&
                condition.apply(criteria.getRetestDate(), copy.getRetestDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
