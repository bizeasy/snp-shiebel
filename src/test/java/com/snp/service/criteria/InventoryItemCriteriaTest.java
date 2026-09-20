package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemCriteriaTest {

    @Test
    void newInventoryItemCriteriaHasAllFiltersNullTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        setAllFilters(inventoryItemCriteria);

        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void inventoryItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        setAllFilters(inventoryItemCriteria);

        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        assertThat(inventoryItemCriteria).hasToString("InventoryItemCriteria{}");
    }

    private static void setAllFilters(InventoryItemCriteria inventoryItemCriteria) {
        inventoryItemCriteria.id();
        inventoryItemCriteria.receivedDate();
        inventoryItemCriteria.manufacturedDate();
        inventoryItemCriteria.expiryDate();
        inventoryItemCriteria.retestDate();
        inventoryItemCriteria.containerId();
        inventoryItemCriteria.batchNo();
        inventoryItemCriteria.mfgBatchNo();
        inventoryItemCriteria.lotNoStr();
        inventoryItemCriteria.binNumber();
        inventoryItemCriteria.comments();
        inventoryItemCriteria.quantityOnHandTotal();
        inventoryItemCriteria.availableToPromiseTotal();
        inventoryItemCriteria.accountingQuantityTotal();
        inventoryItemCriteria.oldQuantityOnHand();
        inventoryItemCriteria.oldAvailableToPromise();
        inventoryItemCriteria.serialNumber();
        inventoryItemCriteria.softIdentifier();
        inventoryItemCriteria.activationNumber();
        inventoryItemCriteria.activationValidThru();
        inventoryItemCriteria.unitCost();
        inventoryItemCriteria.inventoryItemTypeId();
        inventoryItemCriteria.productId();
        inventoryItemCriteria.supplierId();
        inventoryItemCriteria.ownerPartyId();
        inventoryItemCriteria.statusId();
        inventoryItemCriteria.facilityId();
        inventoryItemCriteria.facilityLocationId();
        inventoryItemCriteria.uomId();
        inventoryItemCriteria.currencyUomId();
        inventoryItemCriteria.lotId();
        inventoryItemCriteria.distinct();
    }

    private static Condition<InventoryItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReceivedDate()) &&
                condition.apply(criteria.getManufacturedDate()) &&
                condition.apply(criteria.getExpiryDate()) &&
                condition.apply(criteria.getRetestDate()) &&
                condition.apply(criteria.getContainerId()) &&
                condition.apply(criteria.getBatchNo()) &&
                condition.apply(criteria.getMfgBatchNo()) &&
                condition.apply(criteria.getLotNoStr()) &&
                condition.apply(criteria.getBinNumber()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getQuantityOnHandTotal()) &&
                condition.apply(criteria.getAvailableToPromiseTotal()) &&
                condition.apply(criteria.getAccountingQuantityTotal()) &&
                condition.apply(criteria.getOldQuantityOnHand()) &&
                condition.apply(criteria.getOldAvailableToPromise()) &&
                condition.apply(criteria.getSerialNumber()) &&
                condition.apply(criteria.getSoftIdentifier()) &&
                condition.apply(criteria.getActivationNumber()) &&
                condition.apply(criteria.getActivationValidThru()) &&
                condition.apply(criteria.getUnitCost()) &&
                condition.apply(criteria.getInventoryItemTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getOwnerPartyId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getCurrencyUomId()) &&
                condition.apply(criteria.getLotId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemCriteria> copyFiltersAre(
        InventoryItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReceivedDate(), copy.getReceivedDate()) &&
                condition.apply(criteria.getManufacturedDate(), copy.getManufacturedDate()) &&
                condition.apply(criteria.getExpiryDate(), copy.getExpiryDate()) &&
                condition.apply(criteria.getRetestDate(), copy.getRetestDate()) &&
                condition.apply(criteria.getContainerId(), copy.getContainerId()) &&
                condition.apply(criteria.getBatchNo(), copy.getBatchNo()) &&
                condition.apply(criteria.getMfgBatchNo(), copy.getMfgBatchNo()) &&
                condition.apply(criteria.getLotNoStr(), copy.getLotNoStr()) &&
                condition.apply(criteria.getBinNumber(), copy.getBinNumber()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getQuantityOnHandTotal(), copy.getQuantityOnHandTotal()) &&
                condition.apply(criteria.getAvailableToPromiseTotal(), copy.getAvailableToPromiseTotal()) &&
                condition.apply(criteria.getAccountingQuantityTotal(), copy.getAccountingQuantityTotal()) &&
                condition.apply(criteria.getOldQuantityOnHand(), copy.getOldQuantityOnHand()) &&
                condition.apply(criteria.getOldAvailableToPromise(), copy.getOldAvailableToPromise()) &&
                condition.apply(criteria.getSerialNumber(), copy.getSerialNumber()) &&
                condition.apply(criteria.getSoftIdentifier(), copy.getSoftIdentifier()) &&
                condition.apply(criteria.getActivationNumber(), copy.getActivationNumber()) &&
                condition.apply(criteria.getActivationValidThru(), copy.getActivationValidThru()) &&
                condition.apply(criteria.getUnitCost(), copy.getUnitCost()) &&
                condition.apply(criteria.getInventoryItemTypeId(), copy.getInventoryItemTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getOwnerPartyId(), copy.getOwnerPartyId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getFacilityId(), copy.getFacilityId()) &&
                condition.apply(criteria.getFacilityLocationId(), copy.getFacilityLocationId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getCurrencyUomId(), copy.getCurrencyUomId()) &&
                condition.apply(criteria.getLotId(), copy.getLotId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
