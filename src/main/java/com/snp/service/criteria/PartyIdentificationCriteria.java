package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.PartyIdentification} entity. This class is used
 * in {@link com.snp.web.rest.PartyIdentificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /party-identifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyIdentificationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter idValue;

    private LongFilter partyId;

    private LongFilter typeId;

    private Boolean distinct;

    public PartyIdentificationCriteria() {}

    public PartyIdentificationCriteria(PartyIdentificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.idValue = other.optionalIdValue().map(StringFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.typeId = other.optionalTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PartyIdentificationCriteria copy() {
        return new PartyIdentificationCriteria(this);
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

    public StringFilter getIdValue() {
        return idValue;
    }

    public Optional<StringFilter> optionalIdValue() {
        return Optional.ofNullable(idValue);
    }

    public StringFilter idValue() {
        if (idValue == null) {
            setIdValue(new StringFilter());
        }
        return idValue;
    }

    public void setIdValue(StringFilter idValue) {
        this.idValue = idValue;
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

    public LongFilter getTypeId() {
        return typeId;
    }

    public Optional<LongFilter> optionalTypeId() {
        return Optional.ofNullable(typeId);
    }

    public LongFilter typeId() {
        if (typeId == null) {
            setTypeId(new LongFilter());
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
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
        final PartyIdentificationCriteria that = (PartyIdentificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idValue, that.idValue) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idValue, partyId, typeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyIdentificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdValue().map(f -> "idValue=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalTypeId().map(f -> "typeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
