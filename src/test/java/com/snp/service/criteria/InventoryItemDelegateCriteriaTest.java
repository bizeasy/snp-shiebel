package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemDelegateCriteriaTest {

    @Test
    void newInventoryItemDelegateCriteriaHasAllFiltersNullTest() {
        var inventoryItemDelegateCriteria = new InventoryItemDelegateCriteria();
        assertThat(inventoryItemDelegateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemDelegateCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemDelegateCriteria = new InventoryItemDelegateCriteria();

        setAllFilters(inventoryItemDelegateCriteria);

        assertThat(inventoryItemDelegateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemDelegateCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemDelegateCriteria = new InventoryItemDelegateCriteria();
        var copy = inventoryItemDelegateCriteria.copy();

        assertThat(inventoryItemDelegateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemDelegateCriteria)
        );
    }

    @Test
    void inventoryItemDelegateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemDelegateCriteria = new InventoryItemDelegateCriteria();
        setAllFilters(inventoryItemDelegateCriteria);

        var copy = inventoryItemDelegateCriteria.copy();

        assertThat(inventoryItemDelegateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemDelegateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemDelegateCriteria = new InventoryItemDelegateCriteria();

        assertThat(inventoryItemDelegateCriteria).hasToString("InventoryItemDelegateCriteria{}");
    }

    private static void setAllFilters(InventoryItemDelegateCriteria inventoryItemDelegateCriteria) {
        inventoryItemDelegateCriteria.id();
        inventoryItemDelegateCriteria.sequenceNo();
        inventoryItemDelegateCriteria.effectiveDate();
        inventoryItemDelegateCriteria.quantityOnHandDiff();
        inventoryItemDelegateCriteria.availableToPromiseDiff();
        inventoryItemDelegateCriteria.accountingQuantityDiff();
        inventoryItemDelegateCriteria.unitCost();
        inventoryItemDelegateCriteria.remarks();
        inventoryItemDelegateCriteria.inventoryItemId();
        inventoryItemDelegateCriteria.orderId();
        inventoryItemDelegateCriteria.orderItemId();
        inventoryItemDelegateCriteria.invoiceId();
        inventoryItemDelegateCriteria.invoiceItemId();
        inventoryItemDelegateCriteria.shipmentId();
        inventoryItemDelegateCriteria.shipmentItemId();
        inventoryItemDelegateCriteria.itemIssuanceId();
        inventoryItemDelegateCriteria.receiptId();
        inventoryItemDelegateCriteria.distinct();
    }

    private static Condition<InventoryItemDelegateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getEffectiveDate()) &&
                condition.apply(criteria.getQuantityOnHandDiff()) &&
                condition.apply(criteria.getAvailableToPromiseDiff()) &&
                condition.apply(criteria.getAccountingQuantityDiff()) &&
                condition.apply(criteria.getUnitCost()) &&
                condition.apply(criteria.getRemarks()) &&
                condition.apply(criteria.getInventoryItemId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemId()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId()) &&
                condition.apply(criteria.getItemIssuanceId()) &&
                condition.apply(criteria.getReceiptId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemDelegateCriteria> copyFiltersAre(
        InventoryItemDelegateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getEffectiveDate(), copy.getEffectiveDate()) &&
                condition.apply(criteria.getQuantityOnHandDiff(), copy.getQuantityOnHandDiff()) &&
                condition.apply(criteria.getAvailableToPromiseDiff(), copy.getAvailableToPromiseDiff()) &&
                condition.apply(criteria.getAccountingQuantityDiff(), copy.getAccountingQuantityDiff()) &&
                condition.apply(criteria.getUnitCost(), copy.getUnitCost()) &&
                condition.apply(criteria.getRemarks(), copy.getRemarks()) &&
                condition.apply(criteria.getInventoryItemId(), copy.getInventoryItemId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemId(), copy.getOrderItemId()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemId(), copy.getInvoiceItemId()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getShipmentItemId(), copy.getShipmentItemId()) &&
                condition.apply(criteria.getItemIssuanceId(), copy.getItemIssuanceId()) &&
                condition.apply(criteria.getReceiptId(), copy.getReceiptId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
