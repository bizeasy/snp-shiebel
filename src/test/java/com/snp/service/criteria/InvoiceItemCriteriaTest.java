package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceItemCriteriaTest {

    @Test
    void newInvoiceItemCriteriaHasAllFiltersNullTest() {
        var invoiceItemCriteria = new InvoiceItemCriteria();
        assertThat(invoiceItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceItemCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceItemCriteria = new InvoiceItemCriteria();

        setAllFilters(invoiceItemCriteria);

        assertThat(invoiceItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceItemCriteriaCopyCreatesNullFilterTest() {
        var invoiceItemCriteria = new InvoiceItemCriteria();
        var copy = invoiceItemCriteria.copy();

        assertThat(invoiceItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceItemCriteria)
        );
    }

    @Test
    void invoiceItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceItemCriteria = new InvoiceItemCriteria();
        setAllFilters(invoiceItemCriteria);

        var copy = invoiceItemCriteria.copy();

        assertThat(invoiceItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceItemCriteria = new InvoiceItemCriteria();

        assertThat(invoiceItemCriteria).hasToString("InvoiceItemCriteria{}");
    }

    private static void setAllFilters(InvoiceItemCriteria invoiceItemCriteria) {
        invoiceItemCriteria.id();
        invoiceItemCriteria.sequenceNo();
        invoiceItemCriteria.quantity();
        invoiceItemCriteria.amount();
        invoiceItemCriteria.cancelQuantity();
        invoiceItemCriteria.selectedAmount();
        invoiceItemCriteria.unitPrice();
        invoiceItemCriteria.unitListPrice();
        invoiceItemCriteria.isModifiedPrice();
        invoiceItemCriteria.estimatedShipDate();
        invoiceItemCriteria.estimatedDeliveryDate();
        invoiceItemCriteria.shipBeforeDate();
        invoiceItemCriteria.shipAfterDate();
        invoiceItemCriteria.entryDate();
        invoiceItemCriteria.attr1();
        invoiceItemCriteria.attr2();
        invoiceItemCriteria.attr3();
        invoiceItemCriteria.invoiceId();
        invoiceItemCriteria.invoiceItemTypeId();
        invoiceItemCriteria.productId();
        invoiceItemCriteria.uomId();
        invoiceItemCriteria.productFeatureId();
        invoiceItemCriteria.productFacilityId();
        invoiceItemCriteria.fromInventoryItemId();
        invoiceItemCriteria.taxAuthRateId();
        invoiceItemCriteria.parentInvoiceId();
        invoiceItemCriteria.parentInvoiceItemId();
        invoiceItemCriteria.distinct();
    }

    private static Condition<InvoiceItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getCancelQuantity()) &&
                condition.apply(criteria.getSelectedAmount()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getUnitListPrice()) &&
                condition.apply(criteria.getIsModifiedPrice()) &&
                condition.apply(criteria.getEstimatedShipDate()) &&
                condition.apply(criteria.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getShipBeforeDate()) &&
                condition.apply(criteria.getShipAfterDate()) &&
                condition.apply(criteria.getEntryDate()) &&
                condition.apply(criteria.getAttr1()) &&
                condition.apply(criteria.getAttr2()) &&
                condition.apply(criteria.getAttr3()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getProductFacilityId()) &&
                condition.apply(criteria.getFromInventoryItemId()) &&
                condition.apply(criteria.getTaxAuthRateId()) &&
                condition.apply(criteria.getParentInvoiceId()) &&
                condition.apply(criteria.getParentInvoiceItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceItemCriteria> copyFiltersAre(InvoiceItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getCancelQuantity(), copy.getCancelQuantity()) &&
                condition.apply(criteria.getSelectedAmount(), copy.getSelectedAmount()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getUnitListPrice(), copy.getUnitListPrice()) &&
                condition.apply(criteria.getIsModifiedPrice(), copy.getIsModifiedPrice()) &&
                condition.apply(criteria.getEstimatedShipDate(), copy.getEstimatedShipDate()) &&
                condition.apply(criteria.getEstimatedDeliveryDate(), copy.getEstimatedDeliveryDate()) &&
                condition.apply(criteria.getShipBeforeDate(), copy.getShipBeforeDate()) &&
                condition.apply(criteria.getShipAfterDate(), copy.getShipAfterDate()) &&
                condition.apply(criteria.getEntryDate(), copy.getEntryDate()) &&
                condition.apply(criteria.getAttr1(), copy.getAttr1()) &&
                condition.apply(criteria.getAttr2(), copy.getAttr2()) &&
                condition.apply(criteria.getAttr3(), copy.getAttr3()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getInvoiceItemTypeId(), copy.getInvoiceItemTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getProductFacilityId(), copy.getProductFacilityId()) &&
                condition.apply(criteria.getFromInventoryItemId(), copy.getFromInventoryItemId()) &&
                condition.apply(criteria.getTaxAuthRateId(), copy.getTaxAuthRateId()) &&
                condition.apply(criteria.getParentInvoiceId(), copy.getParentInvoiceId()) &&
                condition.apply(criteria.getParentInvoiceItemId(), copy.getParentInvoiceItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
