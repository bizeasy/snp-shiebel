package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFacilityLocation} entity. This class is used
 * in {@link com.snp.web.rest.ProductFacilityLocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-facility-locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFacilityLocationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter minimumStock;

    private BigDecimalFilter moveQuantity;

    private LongFilter productId;

    private LongFilter facilityId;

    private LongFilter facilityLocationId;

    private Boolean distinct;

    public ProductFacilityLocationCriteria() {}

    public ProductFacilityLocationCriteria(ProductFacilityLocationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.minimumStock = other.optionalMinimumStock().map(BigDecimalFilter::copy).orElse(null);
        this.moveQuantity = other.optionalMoveQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.facilityLocationId = other.optionalFacilityLocationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFacilityLocationCriteria copy() {
        return new ProductFacilityLocationCriteria(this);
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

    public BigDecimalFilter getMinimumStock() {
        return minimumStock;
    }

    public Optional<BigDecimalFilter> optionalMinimumStock() {
        return Optional.ofNullable(minimumStock);
    }

    public BigDecimalFilter minimumStock() {
        if (minimumStock == null) {
            setMinimumStock(new BigDecimalFilter());
        }
        return minimumStock;
    }

    public void setMinimumStock(BigDecimalFilter minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimalFilter getMoveQuantity() {
        return moveQuantity;
    }

    public Optional<BigDecimalFilter> optionalMoveQuantity() {
        return Optional.ofNullable(moveQuantity);
    }

    public BigDecimalFilter moveQuantity() {
        if (moveQuantity == null) {
            setMoveQuantity(new BigDecimalFilter());
        }
        return moveQuantity;
    }

    public void setMoveQuantity(BigDecimalFilter moveQuantity) {
        this.moveQuantity = moveQuantity;
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

    public LongFilter getFacilityId() {
        return facilityId;
    }

    public Optional<LongFilter> optionalFacilityId() {
        return Optional.ofNullable(facilityId);
    }

    public LongFilter facilityId() {
        if (facilityId == null) {
            setFacilityId(new LongFilter());
        }
        return facilityId;
    }

    public void setFacilityId(LongFilter facilityId) {
        this.facilityId = facilityId;
    }

    public LongFilter getFacilityLocationId() {
        return facilityLocationId;
    }

    public Optional<LongFilter> optionalFacilityLocationId() {
        return Optional.ofNullable(facilityLocationId);
    }

    public LongFilter facilityLocationId() {
        if (facilityLocationId == null) {
            setFacilityLocationId(new LongFilter());
        }
        return facilityLocationId;
    }

    public void setFacilityLocationId(LongFilter facilityLocationId) {
        this.facilityLocationId = facilityLocationId;
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
        final ProductFacilityLocationCriteria that = (ProductFacilityLocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(minimumStock, that.minimumStock) &&
            Objects.equals(moveQuantity, that.moveQuantity) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(facilityLocationId, that.facilityLocationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minimumStock, moveQuantity, productId, facilityId, facilityLocationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFacilityLocationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMinimumStock().map(f -> "minimumStock=" + f + ", ").orElse("") +
            optionalMoveQuantity().map(f -> "moveQuantity=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalFacilityLocationId().map(f -> "facilityLocationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
