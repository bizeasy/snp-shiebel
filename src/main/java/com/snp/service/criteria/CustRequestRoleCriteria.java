package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CustRequestRole} entity. This class is used
 * in {@link com.snp.web.rest.CustRequestRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cust-request-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestRoleCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter custRequestId;

    private LongFilter custRequestItemId;

    private LongFilter partyId;

    private LongFilter roleTypeId;

    private Boolean distinct;

    public CustRequestRoleCriteria() {}

    public CustRequestRoleCriteria(CustRequestRoleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.custRequestId = other.optionalCustRequestId().map(LongFilter::copy).orElse(null);
        this.custRequestItemId = other.optionalCustRequestItemId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustRequestRoleCriteria copy() {
        return new CustRequestRoleCriteria(this);
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

    public LongFilter getCustRequestId() {
        return custRequestId;
    }

    public Optional<LongFilter> optionalCustRequestId() {
        return Optional.ofNullable(custRequestId);
    }

    public LongFilter custRequestId() {
        if (custRequestId == null) {
            setCustRequestId(new LongFilter());
        }
        return custRequestId;
    }

    public void setCustRequestId(LongFilter custRequestId) {
        this.custRequestId = custRequestId;
    }

    public LongFilter getCustRequestItemId() {
        return custRequestItemId;
    }

    public Optional<LongFilter> optionalCustRequestItemId() {
        return Optional.ofNullable(custRequestItemId);
    }

    public LongFilter custRequestItemId() {
        if (custRequestItemId == null) {
            setCustRequestItemId(new LongFilter());
        }
        return custRequestItemId;
    }

    public void setCustRequestItemId(LongFilter custRequestItemId) {
        this.custRequestItemId = custRequestItemId;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public Optional<LongFilter> optionalPartyId() {
        return Optional.ofNullable(partyId);
    }

    public LongFilter partyId() {
        if (partyId == null) {
            setPartyId(new LongFilter());
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    public LongFilter getRoleTypeId() {
        return roleTypeId;
    }

    public Optional<LongFilter> optionalRoleTypeId() {
        return Optional.ofNullable(roleTypeId);
    }

    public LongFilter roleTypeId() {
        if (roleTypeId == null) {
            setRoleTypeId(new LongFilter());
        }
        return roleTypeId;
    }

    public void setRoleTypeId(LongFilter roleTypeId) {
        this.roleTypeId = roleTypeId;
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
        final CustRequestRoleCriteria that = (CustRequestRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(custRequestId, that.custRequestId) &&
            Objects.equals(custRequestItemId, that.custRequestItemId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, custRequestId, custRequestItemId, partyId, roleTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestRoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCustRequestId().map(f -> "custRequestId=" + f + ", ").orElse("") +
            optionalCustRequestItemId().map(f -> "custRequestItemId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
