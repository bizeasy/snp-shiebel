package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PartyRelationship} entity. This class is used
 * in {@link com.snp.web.rest.PartyRelationshipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /party-relationships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyRelationshipCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fromDate;

    private InstantFilter thruDate;

    private StringFilter relationshipName;

    private LongFilter partyFromId;

    private LongFilter partyToId;

    private LongFilter roleTypeFromId;

    private LongFilter roleTypeToId;

    private LongFilter relationshipTypeId;

    private Boolean distinct;

    public PartyRelationshipCriteria() {}

    public PartyRelationshipCriteria(PartyRelationshipCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.thruDate = other.optionalThruDate().map(InstantFilter::copy).orElse(null);
        this.relationshipName = other.optionalRelationshipName().map(StringFilter::copy).orElse(null);
        this.partyFromId = other.optionalPartyFromId().map(LongFilter::copy).orElse(null);
        this.partyToId = other.optionalPartyToId().map(LongFilter::copy).orElse(null);
        this.roleTypeFromId = other.optionalRoleTypeFromId().map(LongFilter::copy).orElse(null);
        this.roleTypeToId = other.optionalRoleTypeToId().map(LongFilter::copy).orElse(null);
        this.relationshipTypeId = other.optionalRelationshipTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PartyRelationshipCriteria copy() {
        return new PartyRelationshipCriteria(this);
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

    public StringFilter getRelationshipName() {
        return relationshipName;
    }

    public Optional<StringFilter> optionalRelationshipName() {
        return Optional.ofNullable(relationshipName);
    }

    public StringFilter relationshipName() {
        if (relationshipName == null) {
            setRelationshipName(new StringFilter());
        }
        return relationshipName;
    }

    public void setRelationshipName(StringFilter relationshipName) {
        this.relationshipName = relationshipName;
    }

    public LongFilter getPartyFromId() {
        return partyFromId;
    }

    public Optional<LongFilter> optionalPartyFromId() {
        return Optional.ofNullable(partyFromId);
    }

    public LongFilter partyFromId() {
        if (partyFromId == null) {
            setPartyFromId(new LongFilter());
        }
        return partyFromId;
    }

    public void setPartyFromId(LongFilter partyFromId) {
        this.partyFromId = partyFromId;
    }

    public LongFilter getPartyToId() {
        return partyToId;
    }

    public Optional<LongFilter> optionalPartyToId() {
        return Optional.ofNullable(partyToId);
    }

    public LongFilter partyToId() {
        if (partyToId == null) {
            setPartyToId(new LongFilter());
        }
        return partyToId;
    }

    public void setPartyToId(LongFilter partyToId) {
        this.partyToId = partyToId;
    }

    public LongFilter getRoleTypeFromId() {
        return roleTypeFromId;
    }

    public Optional<LongFilter> optionalRoleTypeFromId() {
        return Optional.ofNullable(roleTypeFromId);
    }

    public LongFilter roleTypeFromId() {
        if (roleTypeFromId == null) {
            setRoleTypeFromId(new LongFilter());
        }
        return roleTypeFromId;
    }

    public void setRoleTypeFromId(LongFilter roleTypeFromId) {
        this.roleTypeFromId = roleTypeFromId;
    }

    public LongFilter getRoleTypeToId() {
        return roleTypeToId;
    }

    public Optional<LongFilter> optionalRoleTypeToId() {
        return Optional.ofNullable(roleTypeToId);
    }

    public LongFilter roleTypeToId() {
        if (roleTypeToId == null) {
            setRoleTypeToId(new LongFilter());
        }
        return roleTypeToId;
    }

    public void setRoleTypeToId(LongFilter roleTypeToId) {
        this.roleTypeToId = roleTypeToId;
    }

    public LongFilter getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public Optional<LongFilter> optionalRelationshipTypeId() {
        return Optional.ofNullable(relationshipTypeId);
    }

    public LongFilter relationshipTypeId() {
        if (relationshipTypeId == null) {
            setRelationshipTypeId(new LongFilter());
        }
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(LongFilter relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
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
        final PartyRelationshipCriteria that = (PartyRelationshipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(thruDate, that.thruDate) &&
            Objects.equals(relationshipName, that.relationshipName) &&
            Objects.equals(partyFromId, that.partyFromId) &&
            Objects.equals(partyToId, that.partyToId) &&
            Objects.equals(roleTypeFromId, that.roleTypeFromId) &&
            Objects.equals(roleTypeToId, that.roleTypeToId) &&
            Objects.equals(relationshipTypeId, that.relationshipTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fromDate,
            thruDate,
            relationshipName,
            partyFromId,
            partyToId,
            roleTypeFromId,
            roleTypeToId,
            relationshipTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationshipCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalThruDate().map(f -> "thruDate=" + f + ", ").orElse("") +
            optionalRelationshipName().map(f -> "relationshipName=" + f + ", ").orElse("") +
            optionalPartyFromId().map(f -> "partyFromId=" + f + ", ").orElse("") +
            optionalPartyToId().map(f -> "partyToId=" + f + ", ").orElse("") +
            optionalRoleTypeFromId().map(f -> "roleTypeFromId=" + f + ", ").orElse("") +
            optionalRoleTypeToId().map(f -> "roleTypeToId=" + f + ", ").orElse("") +
            optionalRelationshipTypeId().map(f -> "relationshipTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
