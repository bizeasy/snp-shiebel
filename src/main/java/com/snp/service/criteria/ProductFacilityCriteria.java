package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ProductFacility} entity. This class is used
 * in {@link com.snp.web.rest.ProductFacilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-facilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductFacilityCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter minimumStock;

    private BigDecimalFilter reorderQty;

    private IntegerFilter daysToShip;

    private BigDecimalFilter lastInventoryCount;

    private LongFilter productId;

    private LongFilter facilityId;

    private Boolean distinct;

    public ProductFacilityCriteria() {}

    public ProductFacilityCriteria(ProductFacilityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.minimumStock = other.optionalMinimumStock().map(BigDecimalFilter::copy).orElse(null);
        this.reorderQty = other.optionalReorderQty().map(BigDecimalFilter::copy).orElse(null);
        this.daysToShip = other.optionalDaysToShip().map(IntegerFilter::copy).orElse(null);
        this.lastInventoryCount = other.optionalLastInventoryCount().map(BigDecimalFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductFacilityCriteria copy() {
        return new ProductFacilityCriteria(this);
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

    public BigDecimalFilter getReorderQty() {
        return reorderQty;
    }

    public Optional<BigDecimalFilter> optionalReorderQty() {
        return Optional.ofNullable(reorderQty);
    }

    public BigDecimalFilter reorderQty() {
        if (reorderQty == null) {
            setReorderQty(new BigDecimalFilter());
        }
        return reorderQty;
    }

    public void setReorderQty(BigDecimalFilter reorderQty) {
        this.reorderQty = reorderQty;
    }

    public IntegerFilter getDaysToShip() {
        return daysToShip;
    }

    public Optional<IntegerFilter> optionalDaysToShip() {
        return Optional.ofNullable(daysToShip);
    }

    public IntegerFilter daysToShip() {
        if (daysToShip == null) {
            setDaysToShip(new IntegerFilter());
        }
        return daysToShip;
    }

    public void setDaysToShip(IntegerFilter daysToShip) {
        this.daysToShip = daysToShip;
    }

    public BigDecimalFilter getLastInventoryCount() {
        return lastInventoryCount;
    }

    public Optional<BigDecimalFilter> optionalLastInventoryCount() {
        return Optional.ofNullable(lastInventoryCount);
    }

    public BigDecimalFilter lastInventoryCount() {
        if (lastInventoryCount == null) {
            setLastInventoryCount(new BigDecimalFilter());
        }
        return lastInventoryCount;
    }

    public void setLastInventoryCount(BigDecimalFilter lastInventoryCount) {
        this.lastInventoryCount = lastInventoryCount;
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
        final ProductFacilityCriteria that = (ProductFacilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(minimumStock, that.minimumStock) &&
            Objects.equals(reorderQty, that.reorderQty) &&
            Objects.equals(daysToShip, that.daysToShip) &&
            Objects.equals(lastInventoryCount, that.lastInventoryCount) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minimumStock, reorderQty, daysToShip, lastInventoryCount, productId, facilityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFacilityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMinimumStock().map(f -> "minimumStock=" + f + ", ").orElse("") +
            optionalReorderQty().map(f -> "reorderQty=" + f + ", ").orElse("") +
            optionalDaysToShip().map(f -> "daysToShip=" + f + ", ").orElse("") +
            optionalLastInventoryCount().map(f -> "lastInventoryCount=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
