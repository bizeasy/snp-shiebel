package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CustRequestAttachment} entity. This class is used
 * in {@link com.snp.web.rest.CustRequestAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cust-request-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequestAttachmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter custRequestId;

    private LongFilter attachmentId;

    private Boolean distinct;

    public CustRequestAttachmentCriteria() {}

    public CustRequestAttachmentCriteria(CustRequestAttachmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.custRequestId = other.optionalCustRequestId().map(LongFilter::copy).orElse(null);
        this.attachmentId = other.optionalAttachmentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustRequestAttachmentCriteria copy() {
        return new CustRequestAttachmentCriteria(this);
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

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public Optional<LongFilter> optionalAttachmentId() {
        return Optional.ofNullable(attachmentId);
    }

    public LongFilter attachmentId() {
        if (attachmentId == null) {
            setAttachmentId(new LongFilter());
        }
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
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
        final CustRequestAttachmentCriteria that = (CustRequestAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(custRequestId, that.custRequestId) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, custRequestId, attachmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequestAttachmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCustRequestId().map(f -> "custRequestId=" + f + ", ").orElse("") +
            optionalAttachmentId().map(f -> "attachmentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
