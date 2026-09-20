package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFeatureAppl} entity. This class is used
 * in {@link com.snp.web.rest.ProductFeatureApplResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-feature-appls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeatureApplCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private IntegerFilter sequenceNo;

    private BigDecimalFilter amount;

    private LongFilter productId;

    private LongFilter productFeatureId;

    private LongFilter featureApplTypeId;

    private Boolean distinct;

    public ProductFeatureApplCriteria() {}

    public ProductFeatureApplCriteria(ProductFeatureApplCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.productFeatureId = other.optionalProductFeatureId().map(LongFilter::copy).orElse(null);
        this.featureApplTypeId = other.optionalFeatureApplTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFeatureApplCriteria copy() {
        return new ProductFeatureApplCriteria(this);
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

    public IntegerFilter getSequenceNo() {
        return sequenceNo;
    }

    public Optional<IntegerFilter> optionalSequenceNo() {
        return Optional.ofNullable(sequenceNo);
    }

    public IntegerFilter sequenceNo() {
        if (sequenceNo == null) {
            setSequenceNo(new IntegerFilter());
        }
        return sequenceNo;
    }

    public void setSequenceNo(IntegerFilter sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getProductFeatureId() {
        return productFeatureId;
    }

    public Optional<LongFilter> optionalProductFeatureId() {
        return Optional.ofNullable(productFeatureId);
    }

    public LongFilter productFeatureId() {
        if (productFeatureId == null) {
            setProductFeatureId(new LongFilter());
        }
        return productFeatureId;
    }

    public void setProductFeatureId(LongFilter productFeatureId) {
        this.productFeatureId = productFeatureId;
    }

    public LongFilter getFeatureApplTypeId() {
        return featureApplTypeId;
    }

    public Optional<LongFilter> optionalFeatureApplTypeId() {
        return Optional.ofNullable(featureApplTypeId);
    }

    public LongFilter featureApplTypeId() {
        if (featureApplTypeId == null) {
            setFeatureApplTypeId(new LongFilter());
        }
        return featureApplTypeId;
    }

    public void setFeatureApplTypeId(LongFilter featureApplTypeId) {
        this.featureApplTypeId = featureApplTypeId;
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
        final ProductFeatureApplCriteria that = (ProductFeatureApplCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(productFeatureId, that.productFeatureId) &&
            Objects.equals(featureApplTypeId, that.featureApplTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, sequenceNo, amount, productId, productFeatureId, featureApplTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeatureApplCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalProductFeatureId().map(f -> "productFeatureId=" + f + ", ").orElse("") +
            optionalFeatureApplTypeId().map(f -> "featureApplTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
