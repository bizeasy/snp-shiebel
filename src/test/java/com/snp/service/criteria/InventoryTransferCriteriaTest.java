package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryTransferCriteriaTest {

    @Test
    void newInventoryTransferCriteriaHasAllFiltersNullTest() {
        var inventoryTransferCriteria = new InventoryTransferCriteria();
        assertThat(inventoryTransferCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryTransferCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryTransferCriteria = new InventoryTransferCriteria();

        setAllFilters(inventoryTransferCriteria);

        assertThat(inventoryTransferCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryTransferCriteriaCopyCreatesNullFilterTest() {
        var inventoryTransferCriteria = new InventoryTransferCriteria();
        var copy = inventoryTransferCriteria.copy();

        assertThat(inventoryTransferCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryTransferCriteria)
        );
    }

    @Test
    void inventoryTransferCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryTransferCriteria = new InventoryTransferCriteria();
        setAllFilters(inventoryTransferCriteria);

        var copy = inventoryTransferCriteria.copy();

        assertThat(inventoryTransferCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryTransferCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryTransferCriteria = new InventoryTransferCriteria();

        assertThat(inventoryTransferCriteria).hasToString("InventoryTransferCriteria{}");
    }

    private static void setAllFilters(InventoryTransferCriteria inventoryTransferCriteria) {
        inventoryTransferCriteria.id();
        inventoryTransferCriteria.sentDate();
        inventoryTransferCriteria.receivedDate();
        inventoryTransferCriteria.comments();
        inventoryTransferCriteria.inventoryItemId();
        inventoryTransferCriteria.statusId();
        inventoryTransferCriteria.facilityId();
        inventoryTransferCriteria.facilityLocationId();
        inventoryTransferCriteria.facilityToId();
        inventoryTransferCriteria.facilityLocationToId();
        inventoryTransferCriteria.issuanceId();
        inventoryTransferCriteria.distinct();
    }

    private static Condition<InventoryTransferCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSentDate()) &&
                condition.apply(criteria.getReceivedDate()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId()) &&
                condition.apply(criteria.getFacilityToId()) &&
                condition.apply(criteria.getFacilityLocationToId()) &&
                condition.apply(criteria.getIssuanceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryTransferCriteria> copyFiltersAre(
        InventoryTransferCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSentDate(), copy.getSentDate()) &&
                condition.apply(criteria.getReceivedDate(), copy.getReceivedDate()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId(), copy.getFacilityLocationId()) &&
                condition.apply(criteria.getFacilityToId(), copy.getFacilityToId()) &&
                condition.apply(criteria.getFacilityLocationToId(), copy.getFacilityLocationToId()) &&
                condition.apply(criteria.getIssuanceId(), copy.getIssuanceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
