package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFeatureCategoryAppl} entity. This class is used
 * in {@link com.snp.web.rest.ProductFeatureCategoryApplResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-feature-category-appls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeatureCategoryApplCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private LongFilter productCategoryId;

    private LongFilter productFeatureCategoryId;

    private Boolean distinct;

    public ProductFeatureCategoryApplCriteria() {}

    public ProductFeatureCategoryApplCriteria(ProductFeatureCategoryApplCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.productCategoryId = other.optionalProductCategoryId().map(LongFilter::copy).orElse(null);
        this.productFeatureCategoryId = other.optionalProductFeatureCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFeatureCategoryApplCriteria copy() {
        return new ProductFeatureCategoryApplCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getThruDate() {
        return thruDate;
    }

    public Optional<InstantFilter> optionalThruDate() {
        return Optional.ofNullable(thruDate);
    }

    public InstantFilter thruDate() {
        if (thruDate == null) {
            setThruDate(new InstantFilter());
        }
        return thruDate;
    }

    public void setThruDate(InstantFilter thruDate) {
        this.thruDate = thruDate;
    }

    public LongFilter getProductCategoryId() {
        return productCategoryId;
    }

    public Optional<LongFilter> optionalProductCategoryId() {
        return Optional.ofNullable(productCategoryId);
    }

    public LongFilter productCategoryId() {
        if (productCategoryId == null) {
            setProductCategoryId(new LongFilter());
        }
        return productCategoryId;
    }

    public void setProductCategoryId(LongFilter productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public LongFilter getProductFeatureCategoryId() {
        return productFeatureCategoryId;
    }

    public Optional<LongFilter> optionalProductFeatureCategoryId() {
        return Optional.ofNullable(productFeatureCategoryId);
    }

    public LongFilter productFeatureCategoryId() {
        if (productFeatureCategoryId == null) {
            setProductFeatureCategoryId(new LongFilter());
        }
        return productFeatureCategoryId;
    }

    public void setProductFeatureCategoryId(LongFilter productFeatureCategoryId) {
        this.productFeatureCategoryId = productFeatureCategoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductFeatureCategoryApplCriteria that = (ProductFeatureCategoryApplCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(productCategoryId, that.productCategoryId) &&
            Objects.equals(productFeatureCategoryId, that.productFeatureCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, productCategoryId, productFeatureCategoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeatureCategoryApplCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalProductCategoryId().map(f -> "productCategoryId=" + f + ", ").orElse("") +
            optionalProductFeatureCategoryId().map(f -> "productFeatureCategoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
