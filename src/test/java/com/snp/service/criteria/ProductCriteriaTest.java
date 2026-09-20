package com.snp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCriteriaTest {

    @Test
    void newProductCriteriaHasAllFiltersNullTest() {
        var productCriteria = new ProductCriteria();
        assertThat(productCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productCriteriaFluentMethodsCreatesFiltersTest() {
        var productCriteria = new ProductCriteria();

        setAllFilters(productCriteria);

        assertThat(productCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productCriteriaCopyCreatesNullFilterTest() {
        var productCriteria = new ProductCriteria();
        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void productCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCriteria = new ProductCriteria();
        setAllFilters(productCriteria);

        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCriteria = new ProductCriteria();

        assertThat(productCriteria).hasToString("ProductCriteria{}");
    }

    private static void setAllFilters(ProductCriteria productCriteria) {
        productCriteria.id();
        productCriteria.name();
        productCriteria.internalName();
        productCriteria.brandName();
        productCriteria.variant();
        productCriteria.sku();
        productCriteria.introductionDate();
        productCriteria.releaseDate();
        productCriteria.quantityIncluded();
        productCriteria.piecesIncluded();
        productCriteria.weight();
        productCriteria.height();
        productCriteria.width();
        productCriteria.depth();
        productCriteria.smallImageUrl();
        productCriteria.mediumImageUrl();
        productCriteria.largeImageUrl();
        productCriteria.detailImageUrl();
        productCriteria.originalImageUrl();
        productCriteria.altImageUrl1();
        productCriteria.altImageUrl2();
        productCriteria.altImageUrl3();
        productCriteria.altImageUrl4();
        productCriteria.altImageUrl5();
        productCriteria.altImageUrl6();
        productCriteria.videoUrl();
        productCriteria.quantity();
        productCriteria.price();
        productCriteria.cgst();
        productCriteria.igst();
        productCriteria.sgst();
        productCriteria.cgstPercentage();
        productCriteria.igstPercentage();
        productCriteria.sgstPercentage();
        productCriteria.totalPrice();
        productCriteria.description();
        productCriteria.longDescription();
        productCriteria.isVirtual();
        productCriteria.isVariant();
        productCriteria.requireInventory();
        productCriteria.returnable();
        productCriteria.isPopular();
        productCriteria.isBanner();
        productCriteria.bannerUrl();
        productCriteria.productTypeId();
        productCriteria.primaryProductCategoryId();
        productCriteria.virtualProductId();
        productCriteria.inventoryItemTypeId();
        productCriteria.taxSlabId();
        productCriteria.uomId();
        productCriteria.quantityUomId();
        productCriteria.weightUomId();
        productCriteria.sizeUomId();
        productCriteria.distinct();
    }

    private static Condition<ProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getInternalName()) &&
                condition.apply(criteria.getBrandName()) &&
                condition.apply(criteria.getVariant()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getIntroductionDate()) &&
                condition.apply(criteria.getReleaseDate()) &&
                condition.apply(criteria.getQuantityIncluded()) &&
                condition.apply(criteria.getPiecesIncluded()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getHeight()) &&
                condition.apply(criteria.getWidth()) &&
                condition.apply(criteria.getDepth()) &&
                condition.apply(criteria.getSmallImageUrl()) &&
                condition.apply(criteria.getMediumImageUrl()) &&
                condition.apply(criteria.getLargeImageUrl()) &&
                condition.apply(criteria.getDetailImageUrl()) &&
                condition.apply(criteria.getOriginalImageUrl()) &&
                condition.apply(criteria.getAltImageUrl1()) &&
                condition.apply(criteria.getAltImageUrl2()) &&
                condition.apply(criteria.getAltImageUrl3()) &&
                condition.apply(criteria.getAltImageUrl4()) &&
                condition.apply(criteria.getAltImageUrl5()) &&
                condition.apply(criteria.getAltImageUrl6()) &&
                condition.apply(criteria.getVideoUrl()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getCgst()) &&
                condition.apply(criteria.getIgst()) &&
                condition.apply(criteria.getSgst()) &&
                condition.apply(criteria.getCgstPercentage()) &&
                condition.apply(criteria.getIgstPercentage()) &&
                condition.apply(criteria.getSgstPercentage()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getLongDescription()) &&
                condition.apply(criteria.getIsVirtual()) &&
                condition.apply(criteria.getIsVariant()) &&
                condition.apply(criteria.getRequireInventory()) &&
                condition.apply(criteria.getReturnable()) &&
                condition.apply(criteria.getIsPopular()) &&
                condition.apply(criteria.getIsBanner()) &&
                condition.apply(criteria.getBannerUrl()) &&
                condition.apply(criteria.getProductTypeId()) &&
                condition.apply(criteria.getPrimaryProductCategoryId()) &&
                condition.apply(criteria.getVirtualProductId()) &&
                condition.apply(criteria.getInventoryItemTypeId()) &&
                condition.apply(criteria.getTaxSlabId()) &&
                condition.apply(criteria.getUomId()) &&
                condition.apply(criteria.getQuantityUomId()) &&
                condition.apply(criteria.getWeightUomId()) &&
                condition.apply(criteria.getSizeUomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCriteria> copyFiltersAre(ProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getInternalName(), copy.getInternalName()) &&
                condition.apply(criteria.getBrandName(), copy.getBrandName()) &&
                condition.apply(criteria.getVariant(), copy.getVariant()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getIntroductionDate(), copy.getIntroductionDate()) &&
                condition.apply(criteria.getReleaseDate(), copy.getReleaseDate()) &&
                condition.apply(criteria.getQuantityIncluded(), copy.getQuantityIncluded()) &&
                condition.apply(criteria.getPiecesIncluded(), copy.getPiecesIncluded()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getHeight(), copy.getHeight()) &&
                condition.apply(criteria.getWidth(), copy.getWidth()) &&
                condition.apply(criteria.getDepth(), copy.getDepth()) &&
                condition.apply(criteria.getSmallImageUrl(), copy.getSmallImageUrl()) &&
                condition.apply(criteria.getMediumImageUrl(), copy.getMediumImageUrl()) &&
                condition.apply(criteria.getLargeImageUrl(), copy.getLargeImageUrl()) &&
                condition.apply(criteria.getDetailImageUrl(), copy.getDetailImageUrl()) &&
                condition.apply(criteria.getOriginalImageUrl(), copy.getOriginalImageUrl()) &&
                condition.apply(criteria.getAltImageUrl1(), copy.getAltImageUrl1()) &&
                condition.apply(criteria.getAltImageUrl2(), copy.getAltImageUrl2()) &&
                condition.apply(criteria.getAltImageUrl3(), copy.getAltImageUrl3()) &&
                condition.apply(criteria.getAltImageUrl4(), copy.getAltImageUrl4()) &&
                condition.apply(criteria.getAltImageUrl5(), copy.getAltImageUrl5()) &&
                condition.apply(criteria.getAltImageUrl6(), copy.getAltImageUrl6()) &&
                condition.apply(criteria.getVideoUrl(), copy.getVideoUrl()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getCgst(), copy.getCgst()) &&
                condition.apply(criteria.getIgst(), copy.getIgst()) &&
                condition.apply(criteria.getSgst(), copy.getSgst()) &&
                condition.apply(criteria.getCgstPercentage(), copy.getCgstPercentage()) &&
                condition.apply(criteria.getIgstPercentage(), copy.getIgstPercentage()) &&
                condition.apply(criteria.getSgstPercentage(), copy.getSgstPercentage()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getLongDescription(), copy.getLongDescription()) &&
                condition.apply(criteria.getIsVirtual(), copy.getIsVirtual()) &&
                condition.apply(criteria.getIsVariant(), copy.getIsVariant()) &&
                condition.apply(criteria.getRequireInventory(), copy.getRequireInventory()) &&
                condition.apply(criteria.getReturnable(), copy.getReturnable()) &&
                condition.apply(criteria.getIsPopular(), copy.getIsPopular()) &&
                condition.apply(criteria.getIsBanner(), copy.getIsBanner()) &&
                condition.apply(criteria.getBannerUrl(), copy.getBannerUrl()) &&
                condition.apply(criteria.getProductTypeId(), copy.getProductTypeId()) &&
                condition.apply(criteria.getPrimaryProductCategoryId(), copy.getPrimaryProductCategoryId()) &&
                condition.apply(criteria.getVirtualProductId(), copy.getVirtualProductId()) &&
                condition.apply(criteria.getInventoryItemTypeId(), copy.getInventoryItemTypeId()) &&
                condition.apply(criteria.getTaxSlabId(), copy.getTaxSlabId()) &&
                condition.apply(criteria.getUomId(), copy.getUomId()) &&
                condition.apply(criteria.getQuantityUomId(), copy.getQuantityUomId()) &&
                condition.apply(criteria.getWeightUomId(), copy.getWeightUomId()) &&
                condition.apply(criteria.getSizeUomId(), copy.getSizeUomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
