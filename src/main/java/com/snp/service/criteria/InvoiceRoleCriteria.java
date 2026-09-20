package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InvoiceRole} entity. This class is used
 * in {@link com.snp.web.rest.InvoiceRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoice-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceRoleCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter invoiceId;

    private LongFilter invoiceItemId;

    private LongFilter partyId;

    private LongFilter roleTypeId;

    private Boolean distinct;

    public InvoiceRoleCriteria() {}

    public InvoiceRoleCriteria(InvoiceRoleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(LongFilter::copy).orElse(null);
        this.invoiceItemId = other.optionalInvoiceItemId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceRoleCriteria copy() {
        return new InvoiceRoleCriteria(this);
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

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public Optional<LongFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new LongFilter());
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getInvoiceItemId() {
        return invoiceItemId;
    }

    public Optional<LongFilter> optionalInvoiceItemId() {
        return Optional.ofNullable(invoiceItemId);
    }

    public LongFilter invoiceItemId() {
        if (invoiceItemId == null) {
            setInvoiceItemId(new LongFilter());
        }
        return invoiceItemId;
    }

    public void setInvoiceItemId(LongFilter invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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
        final InvoiceRoleCriteria that = (InvoiceRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(invoiceItemId, that.invoiceItemId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceId, invoiceItemId, partyId, roleTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceRoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalInvoiceItemId().map(f -> "invoiceItemId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
