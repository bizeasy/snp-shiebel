package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InventoryItemVariance} entity. This class is used
 * in {@link com.snp.web.rest.InventoryItemVarianceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventory-item-variances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemVarianceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter varianceReason;

    private BigDecimalFilter atpVar;

    private BigDecimalFilter qohVar;

    private StringFilter comments;

    private LongFilter inventoryItemId;

    private Boolean distinct;

    public InventoryItemVarianceCriteria() {}

    public InventoryItemVarianceCriteria(InventoryItemVarianceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.varianceReason = other.optionalVarianceReason().map(StringFilter::copy).orElse(null);
        this.atpVar = other.optionalAtpVar().map(BigDecimalFilter::copy).orElse(null);
        this.qohVar = other.optionalQohVar().map(BigDecimalFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryItemVarianceCriteria copy() {
        return new InventoryItemVarianceCriteria(this);
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

    public StringFilter getVarianceReason() {
        return varianceReason;
    }

    public Optional<StringFilter> optionalVarianceReason() {
        return Optional.ofNullable(varianceReason);
    }

    public StringFilter varianceReason() {
        if (varianceReason == null) {
            setVarianceReason(new StringFilter());
        }
        return varianceReason;
    }

    public void setVarianceReason(StringFilter varianceReason) {
        this.varianceReason = varianceReason;
    }

    public BigDecimalFilter getAtpVar() {
        return atpVar;
    }

    public Optional<BigDecimalFilter> optionalAtpVar() {
        return Optional.ofNullable(atpVar);
    }

    public BigDecimalFilter atpVar() {
        if (atpVar == null) {
            setAtpVar(new BigDecimalFilter());
        }
        return atpVar;
    }

    public void setAtpVar(BigDecimalFilter atpVar) {
        this.atpVar = atpVar;
    }

    public BigDecimalFilter getQohVar() {
        return qohVar;
    }

    public Optional<BigDecimalFilter> optionalQohVar() {
        return Optional.ofNullable(qohVar);
    }

    public BigDecimalFilter qohVar() {
        if (qohVar == null) {
            setQohVar(new BigDecimalFilter());
        }
        return qohVar;
    }

    public void setQohVar(BigDecimalFilter qohVar) {
        this.qohVar = qohVar;
    }

    public StringFilter getComments() {
        return comments;
    }

    public Optional<StringFilter> optionalComments() {
        return Optional.ofNullable(comments);
    }

    public StringFilter comments() {
        if (comments == null) {
            setComments(new StringFilter());
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LongFilter getInventoryItemId() {
        return inventoryItemId;
    }

    public Optional<LongFilter> optionalInventoryItemId() {
        return Optional.ofNullable(inventoryItemId);
    }

    public LongFilter inventoryItemId() {
        if (inventoryItemId == null) {
            setInventoryItemId(new LongFilter());
        }
        return inventoryItemId;
    }

    public void setInventoryItemId(LongFilter inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
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
        final InventoryItemVarianceCriteria that = (InventoryItemVarianceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(varianceReason, that.varianceReason) &&
            Objects.equals(atpVar, that.atpVar) &&
            Objects.equals(qohVar, that.qohVar) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, varianceReason, atpVar, qohVar, comments, inventoryItemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemVarianceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVarianceReason().map(f -> "varianceReason=" + f + ", ").orElse("") +
            optionalAtpVar().map(f -> "atpVar=" + f + ", ").orElse("") +
            optionalQohVar().map(f -> "qohVar=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
