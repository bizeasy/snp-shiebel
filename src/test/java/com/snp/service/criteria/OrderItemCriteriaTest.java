package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderItemCriteriaTest {

    @Test
    void newOrderItemCriteriaHasAllFiltersNullTest() {
        var orderItemCriteria = new OrderItemCriteria();
        assertThat(orderItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void orderItemCriteriaFluentMethodsCreatesFiltersTest() {
        var orderItemCriteria = new OrderItemCriteria();

        setAllFilters(orderItemCriteria);

        assertThat(orderItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void orderItemCriteriaCopyCreatesNullFilterTest() {
        var orderItemCriteria = new OrderItemCriteria();
        var copy = orderItemCriteria.copy();

        assertThat(orderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(orderItemCriteria)
        );
    }

    @Test
    void orderItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderItemCriteria = new OrderItemCriteria();
        setAllFilters(orderItemCriteria);

        var copy = orderItemCriteria.copy();

        assertThat(orderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(orderItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderItemCriteria = new OrderItemCriteria();

        assertThat(orderItemCriteria).hasToString("OrderItemCriteria{}");
    }

    private static void setAllFilters(OrderItemCriteria orderItemCriteria) {
        orderItemCriteria.id();
        orderItemCriteria.sequenceNo();
        orderItemCriteria.quantity();
        orderItemCriteria.cancelQuantity();
        orderItemCriteria.selectedAmount();
        orderItemCriteria.unitPrice();
        orderItemCriteria.unitListPrice();
        orderItemCriteria.isModifiedPrice();
        orderItemCriteria.estimatedShipDate();
        orderItemCriteria.estimatedDeliveryDate();
        orderItemCriteria.shipBeforeDate();
        orderItemCriteria.shipAfterDate();
        orderItemCriteria.entryDate();
        orderItemCriteria.attr1();
        orderItemCriteria.attr2();
        orderItemCriteria.attr3();
        orderItemCriteria.orderId();
        orderItemCriteria.orderItemTypeId();
        orderItemCriteria.productId();
        orderItemCriteria.uomId();
        orderItemCriteria.supplierProductId();
        orderItemCriteria.supplierId();
        orderItemCriteria.productFeatureId();
        orderItemCriteria.productFacilityId();
        orderItemCriteria.fromInventoryItemId();
        orderItemCriteria.shoppingListItemId();
        orderItemCriteria.quoteItemId();
        orderItemCriteria.statusId();
        orderItemCriteria.taxAuthRateId();
        orderItemCriteria.distinct();
    }

    private static Condition<OrderItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceNo()) &&
                condition.apply(criteria.getQuantity()) &&
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
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getOrderItemTypeId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getSupplierProductId()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getProductFeatureId()) &&
                condition.apply(criteria.getProductFacilityId()) &&
                condition.apply(criteria.getFromInventoryItemId()) &&
                condition.apply(criteria.getShoppingListItemId()) &&
                condition.apply(criteria.getQuoteItemId()) &&
                condition.apply(criteria.getStatusId()) &&
                condition.apply(criteria.getTaxAuthRateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrderItemCriteria> copyFiltersAre(OrderItemCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceNo(), copy.getSequenceNo()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
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
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getOrderItemTypeId(), copy.getOrderItemTypeId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getSupplierProductId(), copy.getSupplierProductId()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getProductFeatureId(), copy.getProductFeatureId()) &&
                condition.apply(criteria.getProductFacilityId(), copy.getProductFacilityId()) &&
                condition.apply(criteria.getFromInventoryItemId(), copy.getFromInventoryItemId()) &&
                condition.apply(criteria.getShoppingListItemId(), copy.getShoppingListItemId()) &&
                condition.apply(criteria.getQuoteItemId(), copy.getQuoteItemId()) &&
                condition.apply(criteria.getStatusId(), copy.getStatusId()) &&
                condition.apply(criteria.getTaxAuthRateId(), copy.getTaxAuthRateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
