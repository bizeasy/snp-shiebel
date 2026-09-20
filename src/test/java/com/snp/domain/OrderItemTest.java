package com.snp.domain;

import static com.snp.domain.InventoryItemTestSamples.*;
import static com.snp.domain.OrderItemTestSamples.*;
import static com.snp.domain.OrderItemTypeTestSamples.*;
import static com.snp.domain.OrderTestSamples.*;
import static com.snp.domain.PartyTestSamples.*;
import static com.snp.domain.ProductFacilityTestSamples.*;
import static com.snp.domain.ProductFeatureTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.QuoteItemTestSamples.*;
import static com.snp.domain.ShoppingListItemTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.SupplierProductTestSamples.*;
import static com.snp.domain.TaxAuthorityRateTypeTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void orderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderItem.setOrder(orderBack);
        assertThat(orderItem.getOrder()).isEqualTo(orderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }

    @Test
    void orderItemTypeTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        OrderItemType orderItemTypeBack = getOrderItemTypeRandomSampleGenerator();

        orderItem.setOrderItemType(orderItemTypeBack);
        assertThat(orderItem.getOrderItemType()).isEqualTo(orderItemTypeBack);

        orderItem.orderItemType(null);
        assertThat(orderItem.getOrderItemType()).isNull();
    }

    @Test
    void productTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderItem.setProduct(productBack);
        assertThat(orderItem.getProduct()).isEqualTo(productBack);

        orderItem.product(null);
        assertThat(orderItem.getProduct()).isNull();
    }

    @Test
    void uomTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        orderItem.setUom(uomBack);
        assertThat(orderItem.getUom()).isEqualTo(uomBack);

        orderItem.uom(null);
        assertThat(orderItem.getUom()).isNull();
    }

    @Test
    void supplierProductTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        SupplierProduct supplierProductBack = getSupplierProductRandomSampleGenerator();

        orderItem.setSupplierProduct(supplierProductBack);
        assertThat(orderItem.getSupplierProduct()).isEqualTo(supplierProductBack);

        orderItem.supplierProduct(null);
        assertThat(orderItem.getSupplierProduct()).isNull();
    }

    @Test
    void supplierTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        orderItem.setSupplier(partyBack);
        assertThat(orderItem.getSupplier()).isEqualTo(partyBack);

        orderItem.supplier(null);
        assertThat(orderItem.getSupplier()).isNull();
    }

    @Test
    void productFeatureTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        ProductFeature productFeatureBack = getProductFeatureRandomSampleGenerator();

        orderItem.setProductFeature(productFeatureBack);
        assertThat(orderItem.getProductFeature()).isEqualTo(productFeatureBack);

        orderItem.productFeature(null);
        assertThat(orderItem.getProductFeature()).isNull();
    }

    @Test
    void productFacilityTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        ProductFacility productFacilityBack = getProductFacilityRandomSampleGenerator();

        orderItem.setProductFacility(productFacilityBack);
        assertThat(orderItem.getProductFacility()).isEqualTo(productFacilityBack);

        orderItem.productFacility(null);
        assertThat(orderItem.getProductFacility()).isNull();
    }

    @Test
    void fromInventoryItemTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        InventoryItem inventoryItemBack = getInventoryItemRandomSampleGenerator();

        orderItem.setFromInventoryItem(inventoryItemBack);
        assertThat(orderItem.getFromInventoryItem()).isEqualTo(inventoryItemBack);

        orderItem.fromInventoryItem(null);
        assertThat(orderItem.getFromInventoryItem()).isNull();
    }

    @Test
    void shoppingListItemTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        ShoppingListItem shoppingListItemBack = getShoppingListItemRandomSampleGenerator();

        orderItem.setShoppingListItem(shoppingListItemBack);
        assertThat(orderItem.getShoppingListItem()).isEqualTo(shoppingListItemBack);

        orderItem.shoppingListItem(null);
        assertThat(orderItem.getShoppingListItem()).isNull();
    }

    @Test
    void quoteItemTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        QuoteItem quoteItemBack = getQuoteItemRandomSampleGenerator();

        orderItem.setQuoteItem(quoteItemBack);
        assertThat(orderItem.getQuoteItem()).isEqualTo(quoteItemBack);

        orderItem.quoteItem(null);
        assertThat(orderItem.getQuoteItem()).isNull();
    }

    @Test
    void statusTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        orderItem.setStatus(statusBack);
        assertThat(orderItem.getStatus()).isEqualTo(statusBack);

        orderItem.status(null);
        assertThat(orderItem.getStatus()).isNull();
    }

    @Test
    void taxAuthRateTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        TaxAuthorityRateType taxAuthorityRateTypeBack = getTaxAuthorityRateTypeRandomSampleGenerator();

        orderItem.setTaxAuthRate(taxAuthorityRateTypeBack);
        assertThat(orderItem.getTaxAuthRate()).isEqualTo(taxAuthorityRateTypeBack);

        orderItem.taxAuthRate(null);
        assertThat(orderItem.getTaxAuthRate()).isNull();
    }
}
