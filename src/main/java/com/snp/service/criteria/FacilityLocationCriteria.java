package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FacilityLocation} entity. This class is used
 * in {@link com.snp.web.rest.FacilityLocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facility-locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityLocationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter locationSeqId;

    private StringFilter areaId;

    private StringFilter aisleId;

    private StringFilter sectionId;

    private StringFilter levelId;

    private StringFilter positionId;

    private LongFilter facilityId;

    private LongFilter locationTypeId;

    private Boolean distinct;

    public FacilityLocationCriteria() {}

    public FacilityLocationCriteria(FacilityLocationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.locationSeqId = other.optionalLocationSeqId().map(StringFilter::copy).orElse(null);
        this.areaId = other.optionalAreaId().map(StringFilter::copy).orElse(null);
        this.aisleId = other.optionalAisleId().map(StringFilter::copy).orElse(null);
        this.sectionId = other.optionalSectionId().map(StringFilter::copy).orElse(null);
        this.levelId = other.optionalLevelId().map(StringFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(StringFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.locationTypeId = other.optionalLocationTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityLocationCriteria copy() {
        return new FacilityLocationCriteria(this);
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

    public StringFilter getLocationSeqId() {
        return locationSeqId;
    }

    public Optional<StringFilter> optionalLocationSeqId() {
        return Optional.ofNullable(locationSeqId);
    }

    public StringFilter locationSeqId() {
        if (locationSeqId == null) {
            setLocationSeqId(new StringFilter());
        }
        return locationSeqId;
    }

    public void setLocationSeqId(StringFilter locationSeqId) {
        this.locationSeqId = locationSeqId;
    }

    public StringFilter getAreaId() {
        return areaId;
    }

    public Optional<StringFilter> optionalAreaId() {
        return Optional.ofNullable(areaId);
    }

    public StringFilter areaId() {
        if (areaId == null) {
            setAreaId(new StringFilter());
        }
        return areaId;
    }

    public void setAreaId(StringFilter areaId) {
        this.areaId = areaId;
    }

    public StringFilter getAisleId() {
        return aisleId;
    }

    public Optional<StringFilter> optionalAisleId() {
        return Optional.ofNullable(aisleId);
    }

    public StringFilter aisleId() {
        if (aisleId == null) {
            setAisleId(new StringFilter());
        }
        return aisleId;
    }

    public void setAisleId(StringFilter aisleId) {
        this.aisleId = aisleId;
    }

    public StringFilter getSectionId() {
        return sectionId;
    }

    public Optional<StringFilter> optionalSectionId() {
        return Optional.ofNullable(sectionId);
    }

    public StringFilter sectionId() {
        if (sectionId == null) {
            setSectionId(new StringFilter());
        }
        return sectionId;
    }

    public void setSectionId(StringFilter sectionId) {
        this.sectionId = sectionId;
    }

    public StringFilter getLevelId() {
        return levelId;
    }

    public Optional<StringFilter> optionalLevelId() {
        return Optional.ofNullable(levelId);
    }

    public StringFilter levelId() {
        if (levelId == null) {
            setLevelId(new StringFilter());
        }
        return levelId;
    }

    public void setLevelId(StringFilter levelId) {
        this.levelId = levelId;
    }

    public StringFilter getPositionId() {
        return positionId;
    }

    public Optional<StringFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public StringFilter positionId() {
        if (positionId == null) {
            setPositionId(new StringFilter());
        }
        return positionId;
    }

    public void setPositionId(StringFilter positionId) {
        this.positionId = positionId;
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

    public LongFilter getLocationTypeId() {
        return locationTypeId;
    }

    public Optional<LongFilter> optionalLocationTypeId() {
        return Optional.ofNullable(locationTypeId);
    }

    public LongFilter locationTypeId() {
        if (locationTypeId == null) {
            setLocationTypeId(new LongFilter());
        }
        return locationTypeId;
    }

    public void setLocationTypeId(LongFilter locationTypeId) {
        this.locationTypeId = locationTypeId;
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
        final FacilityLocationCriteria that = (FacilityLocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(locationSeqId, that.locationSeqId) &&
            Objects.equals(areaId, that.areaId) &&
            Objects.equals(aisleId, that.aisleId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(levelId, that.levelId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(locationTypeId, that.locationTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locationSeqId, areaId, aisleId, sectionId, levelId, positionId, facilityId, locationTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityLocationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLocationSeqId().map(f -> "locationSeqId=" + f + ", ").orElse("") +
            optionalAreaId().map(f -> "areaId=" + f + ", ").orElse("") +
            optionalAisleId().map(f -> "aisleId=" + f + ", ").orElse("") +
            optionalSectionId().map(f -> "sectionId=" + f + ", ").orElse("") +
            optionalLevelId().map(f -> "levelId=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalLocationTypeId().map(f -> "locationTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
