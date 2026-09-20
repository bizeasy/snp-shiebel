package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FacilityGroup} entity. This class is used
 * in {@link com.snp.web.rest.FacilityGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facility-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityGroupCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter facilityGroupTypeId;

    private LongFilter parentFacilityGroupId;

    private Boolean distinct;

    public FacilityGroupCriteria() {}

    public FacilityGroupCriteria(FacilityGroupCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.facilityGroupTypeId = other.optionalFacilityGroupTypeId().map(LongFilter::copy).orElse(null);
        this.parentFacilityGroupId = other.optionalParentFacilityGroupId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityGroupCriteria copy() {
        return new FacilityGroupCriteria(this);
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

    public LongFilter getFacilityGroupTypeId() {
        return facilityGroupTypeId;
    }

    public Optional<LongFilter> optionalFacilityGroupTypeId() {
        return Optional.ofNullable(facilityGroupTypeId);
    }

    public LongFilter facilityGroupTypeId() {
        if (facilityGroupTypeId == null) {
            setFacilityGroupTypeId(new LongFilter());
        }
        return facilityGroupTypeId;
    }

    public void setFacilityGroupTypeId(LongFilter facilityGroupTypeId) {
        this.facilityGroupTypeId = facilityGroupTypeId;
    }

    public LongFilter getParentFacilityGroupId() {
        return parentFacilityGroupId;
    }

    public Optional<LongFilter> optionalParentFacilityGroupId() {
        return Optional.ofNullable(parentFacilityGroupId);
    }

    public LongFilter parentFacilityGroupId() {
        if (parentFacilityGroupId == null) {
            setParentFacilityGroupId(new LongFilter());
        }
        return parentFacilityGroupId;
    }

    public void setParentFacilityGroupId(LongFilter parentFacilityGroupId) {
        this.parentFacilityGroupId = parentFacilityGroupId;
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
        final FacilityGroupCriteria that = (FacilityGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(facilityGroupTypeId, that.facilityGroupTypeId) &&
            Objects.equals(parentFacilityGroupId, that.parentFacilityGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, facilityGroupTypeId, parentFacilityGroupId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityGroupCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFacilityGroupTypeId().map(f -> "facilityGroupTypeId=" + f + ", ").orElse("") +
            optionalParentFacilityGroupId().map(f -> "parentFacilityGroupId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
