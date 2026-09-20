package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.QuoteRole} entity. This class is used
 * in {@link com.snp.web.rest.QuoteRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quote-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteRoleCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter quoteId;

    private LongFilter quoteItemId;

    private LongFilter partyId;

    private LongFilter roleTypeId;

    private Boolean distinct;

    public QuoteRoleCriteria() {}

    public QuoteRoleCriteria(QuoteRoleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(LongFilter::copy).orElse(null);
        this.quoteItemId = other.optionalQuoteItemId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuoteRoleCriteria copy() {
        return new QuoteRoleCriteria(this);
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

    public LongFilter getQuoteId() {
        return quoteId;
    }

    public Optional<LongFilter> optionalQuoteId() {
        return Optional.ofNullable(quoteId);
    }

    public LongFilter quoteId() {
        if (quoteId == null) {
            setQuoteId(new LongFilter());
        }
        return quoteId;
    }

    public void setQuoteId(LongFilter quoteId) {
        this.quoteId = quoteId;
    }

    public LongFilter getQuoteItemId() {
        return quoteItemId;
    }

    public Optional<LongFilter> optionalQuoteItemId() {
        return Optional.ofNullable(quoteItemId);
    }

    public LongFilter quoteItemId() {
        if (quoteItemId == null) {
            setQuoteItemId(new LongFilter());
        }
        return quoteItemId;
    }

    public void setQuoteItemId(LongFilter quoteItemId) {
        this.quoteItemId = quoteItemId;
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
        final QuoteRoleCriteria that = (QuoteRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(quoteItemId, that.quoteItemId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quoteId, quoteItemId, partyId, roleTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteRoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalQuoteItemId().map(f -> "quoteItemId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
