package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Facility} entity. This class is used
 * in {@link com.snp.web.rest.FacilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BigDecimalFilter facilitySize;

    private LongFilter facilityTypeId;

    private LongFilter parentFacilityId;

    private LongFilter ownerPartyId;

    private LongFilter facilityGroupId;

    private LongFilter sizeUomId;

    private LongFilter geoPointId;

    private LongFilter inventoryItemTypeId;

    private LongFilter productStoreId;

    private Boolean distinct;

    public FacilityCriteria() {}

    public FacilityCriteria(FacilityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.facilitySize = other.optionalFacilitySize().map(BigDecimalFilter::copy).orElse(null);
        this.facilityTypeId = other.optionalFacilityTypeId().map(LongFilter::copy).orElse(null);
        this.parentFacilityId = other.optionalParentFacilityId().map(LongFilter::copy).orElse(null);
        this.ownerPartyId = other.optionalOwnerPartyId().map(LongFilter::copy).orElse(null);
        this.facilityGroupId = other.optionalFacilityGroupId().map(LongFilter::copy).orElse(null);
        this.sizeUomId = other.optionalSizeUomId().map(LongFilter::copy).orElse(null);
        this.geoPointId = other.optionalGeoPointId().map(LongFilter::copy).orElse(null);
        this.inventoryItemTypeId = other.optionalInventoryItemTypeId().map(LongFilter::copy).orElse(null);
        this.productStoreId = other.optionalProductStoreId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityCriteria copy() {
        return new FacilityCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getFacilitySize() {
        return facilitySize;
    }

    public Optional<BigDecimalFilter> optionalFacilitySize() {
        return Optional.ofNullable(facilitySize);
    }

    public BigDecimalFilter facilitySize() {
        if (facilitySize == null) {
            setFacilitySize(new BigDecimalFilter());
        }
        return facilitySize;
    }

    public void setFacilitySize(BigDecimalFilter facilitySize) {
        this.facilitySize = facilitySize;
    }

    public LongFilter getFacilityTypeId() {
        return facilityTypeId;
    }

    public Optional<LongFilter> optionalFacilityTypeId() {
        return Optional.ofNullable(facilityTypeId);
    }

    public LongFilter facilityTypeId() {
        if (facilityTypeId == null) {
            setFacilityTypeId(new LongFilter());
        }
        return facilityTypeId;
    }

    public void setFacilityTypeId(LongFilter facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public LongFilter getParentFacilityId() {
        return parentFacilityId;
    }

    public Optional<LongFilter> optionalParentFacilityId() {
        return Optional.ofNullable(parentFacilityId);
    }

    public LongFilter parentFacilityId() {
        if (parentFacilityId == null) {
            setParentFacilityId(new LongFilter());
        }
        return parentFacilityId;
    }

    public void setParentFacilityId(LongFilter parentFacilityId) {
        this.parentFacilityId = parentFacilityId;
    }

    public LongFilter getOwnerPartyId() {
        return ownerPartyId;
    }

    public Optional<LongFilter> optionalOwnerPartyId() {
        return Optional.ofNullable(ownerPartyId);
    }

    public LongFilter ownerPartyId() {
        if (ownerPartyId == null) {
            setOwnerPartyId(new LongFilter());
        }
        return ownerPartyId;
    }

    public void setOwnerPartyId(LongFilter ownerPartyId) {
        this.ownerPartyId = ownerPartyId;
    }

    public LongFilter getFacilityGroupId() {
        return facilityGroupId;
    }

    public Optional<LongFilter> optionalFacilityGroupId() {
        return Optional.ofNullable(facilityGroupId);
    }

    public LongFilter facilityGroupId() {
        if (facilityGroupId == null) {
            setFacilityGroupId(new LongFilter());
        }
        return facilityGroupId;
    }

    public void setFacilityGroupId(LongFilter facilityGroupId) {
        this.facilityGroupId = facilityGroupId;
    }

    public LongFilter getSizeUomId() {
        return sizeUomId;
    }

    public Optional<LongFilter> optionalSizeUomId() {
        return Optional.ofNullable(sizeUomId);
    }

    public LongFilter sizeUomId() {
        if (sizeUomId == null) {
            setSizeUomId(new LongFilter());
        }
        return sizeUomId;
    }

    public void setSizeUomId(LongFilter sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    public LongFilter getGeoPointId() {
        return geoPointId;
    }

    public Optional<LongFilter> optionalGeoPointId() {
        return Optional.ofNullable(geoPointId);
    }

    public LongFilter geoPointId() {
        if (geoPointId == null) {
            setGeoPointId(new LongFilter());
        }
        return geoPointId;
    }

    public void setGeoPointId(LongFilter geoPointId) {
        this.geoPointId = geoPointId;
    }

    public LongFilter getInventoryItemTypeId() {
        return inventoryItemTypeId;
    }

    public Optional<LongFilter> optionalInventoryItemTypeId() {
        return Optional.ofNullable(inventoryItemTypeId);
    }

    public LongFilter inventoryItemTypeId() {
        if (inventoryItemTypeId == null) {
            setInventoryItemTypeId(new LongFilter());
        }
        return inventoryItemTypeId;
    }

    public void setInventoryItemTypeId(LongFilter inventoryItemTypeId) {
        this.inventoryItemTypeId = inventoryItemTypeId;
    }

    public LongFilter getProductStoreId() {
        return productStoreId;
    }

    public Optional<LongFilter> optionalProductStoreId() {
        return Optional.ofNullable(productStoreId);
    }

    public LongFilter productStoreId() {
        if (productStoreId == null) {
            setProductStoreId(new LongFilter());
        }
        return productStoreId;
    }

    public void setProductStoreId(LongFilter productStoreId) {
        this.productStoreId = productStoreId;
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
        final FacilityCriteria that = (FacilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(facilitySize, that.facilitySize) &&
            Objects.equals(facilityTypeId, that.facilityTypeId) &&
            Objects.equals(parentFacilityId, that.parentFacilityId) &&
            Objects.equals(ownerPartyId, that.ownerPartyId) &&
            Objects.equals(facilityGroupId, that.facilityGroupId) &&
            Objects.equals(sizeUomId, that.sizeUomId) &&
            Objects.equals(geoPointId, that.geoPointId) &&
            Objects.equals(inventoryItemTypeId, that.inventoryItemTypeId) &&
            Objects.equals(productStoreId, that.productStoreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            facilitySize,
            facilityTypeId,
            parentFacilityId,
            ownerPartyId,
            facilityGroupId,
            sizeUomId,
            geoPointId,
            inventoryItemTypeId,
            productStoreId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFacilitySize().map(f -> "facilitySize=" + f + ", ").orElse("") +
            optionalFacilityTypeId().map(f -> "facilityTypeId=" + f + ", ").orElse("") +
            optionalParentFacilityId().map(f -> "parentFacilityId=" + f + ", ").orElse("") +
            optionalOwnerPartyId().map(f -> "ownerPartyId=" + f + ", ").orElse("") +
            optionalFacilityGroupId().map(f -> "facilityGroupId=" + f + ", ").orElse("") +
            optionalSizeUomId().map(f -> "sizeUomId=" + f + ", ").orElse("") +
            optionalGeoPointId().map(f -> "geoPointId=" + f + ", ").orElse("") +
            optionalInventoryItemTypeId().map(f -> "inventoryItemTypeId=" + f + ", ").orElse("") +
            optionalProductStoreId().map(f -> "productStoreId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
