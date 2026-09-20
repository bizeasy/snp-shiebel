package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FacilityContactMech} entity. This class is used
 * in {@link com.snp.web.rest.FacilityContactMechResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facility-contact-meches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityContactMechCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private LongFilter facilityId;

    private LongFilter contactMechId;

    private LongFilter contactMechPurposeId;

    private Boolean distinct;

    public FacilityContactMechCriteria() {}

    public FacilityContactMechCriteria(FacilityContactMechCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.contactMechId = other.optionalContactMechId().map(LongFilter::copy).orElse(null);
        this.contactMechPurposeId = other.optionalContactMechPurposeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityContactMechCriteria copy() {
        return new FacilityContactMechCriteria(this);
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

    public LongFilter getContactMechId() {
        return contactMechId;
    }

    public Optional<LongFilter> optionalContactMechId() {
        return Optional.ofNullable(contactMechId);
    }

    public LongFilter contactMechId() {
        if (contactMechId == null) {
            setContactMechId(new LongFilter());
        }
        return contactMechId;
    }

    public void setContactMechId(LongFilter contactMechId) {
        this.contactMechId = contactMechId;
    }

    public LongFilter getContactMechPurposeId() {
        return contactMechPurposeId;
    }

    public Optional<LongFilter> optionalContactMechPurposeId() {
        return Optional.ofNullable(contactMechPurposeId);
    }

    public LongFilter contactMechPurposeId() {
        if (contactMechPurposeId == null) {
            setContactMechPurposeId(new LongFilter());
        }
        return contactMechPurposeId;
    }

    public void setContactMechPurposeId(LongFilter contactMechPurposeId) {
        this.contactMechPurposeId = contactMechPurposeId;
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
        final FacilityContactMechCriteria that = (FacilityContactMechCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(contactMechId, that.contactMechId) &&
            Objects.equals(contactMechPurposeId, that.contactMechPurposeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromDate, thruDate, facilityId, contactMechId, contactMechPurposeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityContactMechCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalContactMechId().map(f -> "contactMechId=" + f + ", ").orElse("") +
            optionalContactMechPurposeId().map(f -> "contactMechPurposeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
