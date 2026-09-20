package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InvoiceItemTypeMap} entity. This class is used
 * in {@link com.snp.web.rest.InvoiceItemTypeMapResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoice-item-type-maps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItemTypeMapCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invoiceItemMapKey;

    private LongFilter invoiceTypeId;

    private LongFilter invoiceItemTypeId;

    private Boolean distinct;

    public InvoiceItemTypeMapCriteria() {}

    public InvoiceItemTypeMapCriteria(InvoiceItemTypeMapCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.invoiceItemMapKey = other.optionalInvoiceItemMapKey().map(StringFilter::copy).orElse(null);
        this.invoiceTypeId = other.optionalInvoiceTypeId().map(LongFilter::copy).orElse(null);
        this.invoiceItemTypeId = other.optionalInvoiceItemTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceItemTypeMapCriteria copy() {
        return new InvoiceItemTypeMapCriteria(this);
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

    public StringFilter getInvoiceItemMapKey() {
        return invoiceItemMapKey;
    }

    public Optional<StringFilter> optionalInvoiceItemMapKey() {
        return Optional.ofNullable(invoiceItemMapKey);
    }

    public StringFilter invoiceItemMapKey() {
        if (invoiceItemMapKey == null) {
            setInvoiceItemMapKey(new StringFilter());
        }
        return invoiceItemMapKey;
    }

    public void setInvoiceItemMapKey(StringFilter invoiceItemMapKey) {
        this.invoiceItemMapKey = invoiceItemMapKey;
    }

    public LongFilter getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public Optional<LongFilter> optionalInvoiceTypeId() {
        return Optional.ofNullable(invoiceTypeId);
    }

    public LongFilter invoiceTypeId() {
        if (invoiceTypeId == null) {
            setInvoiceTypeId(new LongFilter());
        }
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(LongFilter invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public LongFilter getInvoiceItemTypeId() {
        return invoiceItemTypeId;
    }

    public Optional<LongFilter> optionalInvoiceItemTypeId() {
        return Optional.ofNullable(invoiceItemTypeId);
    }

    public LongFilter invoiceItemTypeId() {
        if (invoiceItemTypeId == null) {
            setInvoiceItemTypeId(new LongFilter());
        }
        return invoiceItemTypeId;
    }

    public void setInvoiceItemTypeId(LongFilter invoiceItemTypeId) {
        this.invoiceItemTypeId = invoiceItemTypeId;
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
        final InvoiceItemTypeMapCriteria that = (InvoiceItemTypeMapCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceItemMapKey, that.invoiceItemMapKey) &&
            Objects.equals(invoiceTypeId, that.invoiceTypeId) &&
            Objects.equals(invoiceItemTypeId, that.invoiceItemTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceItemMapKey, invoiceTypeId, invoiceItemTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemTypeMapCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceItemMapKey().map(f -> "invoiceItemMapKey=" + f + ", ").orElse("") +
            optionalInvoiceTypeId().map(f -> "invoiceTypeId=" + f + ", ").orElse("") +
            optionalInvoiceItemTypeId().map(f -> "invoiceItemTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
