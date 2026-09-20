package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFeatureGroupAppl} entity. This class is used
 * in {@link com.snp.web.rest.ProductFeatureGroupApplResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-feature-group-appls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFeatureGroupApplCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private IntegerFilter sequenceNo;

    private LongFilter productFeatureGroupId;

    private LongFilter productFeatureId;

    private Boolean distinct;

    public ProductFeatureGroupApplCriteria() {}

    public ProductFeatureGroupApplCriteria(ProductFeatureGroupApplCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.productFeatureGroupId = other.optionalProductFeatureGroupId().map(LongFilter::copy).orElse(null);
        this.productFeatureId = other.optionalProductFeatureId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFeatureGroupApplCriteria copy() {
        return new ProductFeatureGroupApplCriteria(this);
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

    public LongFilter getProductFeatureGroupId() {
        return productFeatureGroupId;
    }

    public Optional<LongFilter> optionalProductFeatureGroupId() {
        return Optional.ofNullable(productFeatureGroupId);
    }

    public LongFilter productFeatureGroupId() {
        if (productFeatureGroupId == null) {
            setProductFeatureGroupId(new LongFilter());
        }
        return productFeatureGroupId;
    }

    public void setProductFeatureGroupId(LongFilter productFeatureGroupId) {
        this.productFeatureGroupId = productFeatureGroupId;
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
        final ProductFeatureGroupApplCriteria that = (ProductFeatureGroupApplCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(productFeatureGroupId, that.productFeatureGroupId) &&
            Objects.equals(productFeatureId, that.productFeatureId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, sequenceNo, productFeatureGroupId, productFeatureId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeatureGroupApplCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalProductFeatureGroupId().map(f -> "productFeatureGroupId=" + f + ", ").orElse("") +
            optionalProductFeatureId().map(f -> "productFeatureId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
