package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.ContactMech} entity. This class is used
 * in {@link com.snp.web.rest.ContactMechResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-meches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactMechCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter infoString;

    private LongFilter contactMechTypeId;

    private Boolean distinct;

    public ContactMechCriteria() {}

    public ContactMechCriteria(ContactMechCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.infoString = other.optionalInfoString().map(StringFilter::copy).orElse(null);
        this.contactMechTypeId = other.optionalContactMechTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContactMechCriteria copy() {
        return new ContactMechCriteria(this);
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

    public StringFilter getInfoString() {
        return infoString;
    }

    public Optional<StringFilter> optionalInfoString() {
        return Optional.ofNullable(infoString);
    }

    public StringFilter infoString() {
        if (infoString == null) {
            setInfoString(new StringFilter());
        }
        return infoString;
    }

    public void setInfoString(StringFilter infoString) {
        this.infoString = infoString;
    }

    public LongFilter getContactMechTypeId() {
        return contactMechTypeId;
    }

    public Optional<LongFilter> optionalContactMechTypeId() {
        return Optional.ofNullable(contactMechTypeId);
    }

    public LongFilter contactMechTypeId() {
        if (contactMechTypeId == null) {
            setContactMechTypeId(new LongFilter());
        }
        return contactMechTypeId;
    }

    public void setContactMechTypeId(LongFilter contactMechTypeId) {
        this.contactMechTypeId = contactMechTypeId;
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
        final ContactMechCriteria that = (ContactMechCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(infoString, that.infoString) &&
            Objects.equals(contactMechTypeId, that.contactMechTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, infoString, contactMechTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactMechCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInfoString().map(f -> "infoString=" + f + ", ").orElse("") +
            optionalContactMechTypeId().map(f -> "contactMechTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
