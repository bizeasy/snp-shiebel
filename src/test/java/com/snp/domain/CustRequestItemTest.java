package com.snp.domain;

import static com.snp.domain.CustRequestItemTestSamples.*;
import static com.snp.domain.CustRequestTestSamples.*;
import static com.snp.domain.ProductTestSamples.*;
import static com.snp.domain.ShoppingListItemTestSamples.*;
import static com.snp.domain.StatusTestSamples.*;
import static com.snp.domain.UomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.snp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustRequestItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustRequestItem.class);
        CustRequestItem custRequestItem1 = getCustRequestItemSample1();
        CustRequestItem custRequestItem2 = new CustRequestItem();
        assertThat(custRequestItem1).isNotEqualTo(custRequestItem2);

        custRequestItem2.setId(custRequestItem1.getId());
        assertThat(custRequestItem1).isEqualTo(custRequestItem2);

        custRequestItem2 = getCustRequestItemSample2();
        assertThat(custRequestItem1).isNotEqualTo(custRequestItem2);
    }

    @Test
    void custRequestTest() {
        CustRequestItem custRequestItem = getCustRequestItemRandomSampleGenerator();
        CustRequest custRequestBack = getCustRequestRandomSampleGenerator();

        custRequestItem.setCustRequest(custRequestBack);
        assertThat(custRequestItem.getCustRequest()).isEqualTo(custRequestBack);

        custRequestItem.custRequest(null);
        assertThat(custRequestItem.getCustRequest()).isNull();
    }

    @Test
    void statusTest() {
        CustRequestItem custRequestItem = getCustRequestItemRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        custRequestItem.setStatus(statusBack);
        assertThat(custRequestItem.getStatus()).isEqualTo(statusBack);

        custRequestItem.status(null);
        assertThat(custRequestItem.getStatus()).isNull();
    }

    @Test
    void productTest() {
        CustRequestItem custRequestItem = getCustRequestItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        custRequestItem.setProduct(productBack);
        assertThat(custRequestItem.getProduct()).isEqualTo(productBack);

        custRequestItem.product(null);
        assertThat(custRequestItem.getProduct()).isNull();
    }

    @Test
    void uomTest() {
        CustRequestItem custRequestItem = getCustRequestItemRandomSampleGenerator();
        Uom uomBack = getUomRandomSampleGenerator();

        custRequestItem.setUom(uomBack);
        assertThat(custRequestItem.getUom()).isEqualTo(uomBack);

        custRequestItem.uom(null);
        assertThat(custRequestItem.getUom()).isNull();
    }

    @Test
    void shoppingListItemTest() {
        CustRequestItem custRequestItem = getCustRequestItemRandomSampleGenerator();
        ShoppingListItem shoppingListItemBack = getShoppingListItemRandomSampleGenerator();

        custRequestItem.setShoppingListItem(shoppingListItemBack);
        assertThat(custRequestItem.getShoppingListItem()).isEqualTo(shoppingListItemBack);

        custRequestItem.shoppingListItem(null);
        assertThat(custRequestItem.getShoppingListItem()).isNull();
    }
}
