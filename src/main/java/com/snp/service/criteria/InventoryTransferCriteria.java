package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InventoryTransfer} entity. This class is used
 * in {@link com.snp.web.rest.InventoryTransferResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventory-transfers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryTransferCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter sentDate;

    private InstantFilter receivedDate;

    private StringFilter comments;

    private LongFilter inventoryItemId;

    private LongFilter statusId;

    private LongFilter facilityId;

    private LongFilter facilityLocationId;

    private LongFilter facilityToId;

    private LongFilter facilityLocationToId;

    private LongFilter issuanceId;

    private Boolean distinct;

    public InventoryTransferCriteria() {}

    public InventoryTransferCriteria(InventoryTransferCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sentDate = other.optionalSentDate().map(InstantFilter::copy).orElse(null);
        this.receivedDate = other.optionalReceivedDate().map(InstantFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.inventoryItemId = other.optionalInventoryItemId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.facilityLocationId = other.optionalFacilityLocationId().map(LongFilter::copy).orElse(null);
        this.facilityToId = other.optionalFacilityToId().map(LongFilter::copy).orElse(null);
        this.facilityLocationToId = other.optionalFacilityLocationToId().map(LongFilter::copy).orElse(null);
        this.issuanceId = other.optionalIssuanceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryTransferCriteria copy() {
        return new InventoryTransferCriteria(this);
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

    public InstantFilter getSentDate() {
        return sentDate;
    }

    public Optional<InstantFilter> optionalSentDate() {
        return Optional.ofNullable(sentDate);
    }

    public InstantFilter sentDate() {
        if (sentDate == null) {
            setSentDate(new InstantFilter());
        }
        return sentDate;
    }

    public void setSentDate(InstantFilter sentDate) {
        this.sentDate = sentDate;
    }

    public InstantFilter getReceivedDate() {
        return receivedDate;
    }

    public Optional<InstantFilter> optionalReceivedDate() {
        return Optional.ofNullable(receivedDate);
    }

    public InstantFilter receivedDate() {
        if (receivedDate == null) {
            setReceivedDate(new InstantFilter());
        }
        return receivedDate;
    }

    public void setReceivedDate(InstantFilter receivedDate) {
        this.receivedDate = receivedDate;
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

    public LongFilter getStatusId() {
        return statusId;
    }

    public Optional<LongFilter> optionalStatusId() {
        return Optional.ofNullable(statusId);
    }

    public LongFilter statusId() {
        if (statusId == null) {
            setStatusId(new LongFilter());
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
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

    public LongFilter getFacilityLocationId() {
        return facilityLocationId;
    }

    public Optional<LongFilter> optionalFacilityLocationId() {
        return Optional.ofNullable(facilityLocationId);
    }

    public LongFilter facilityLocationId() {
        if (facilityLocationId == null) {
            setFacilityLocationId(new LongFilter());
        }
        return facilityLocationId;
    }

    public void setFacilityLocationId(LongFilter facilityLocationId) {
        this.facilityLocationId = facilityLocationId;
    }

    public LongFilter getFacilityToId() {
        return facilityToId;
    }

    public Optional<LongFilter> optionalFacilityToId() {
        return Optional.ofNullable(facilityToId);
    }

    public LongFilter facilityToId() {
        if (facilityToId == null) {
            setFacilityToId(new LongFilter());
        }
        return facilityToId;
    }

    public void setFacilityToId(LongFilter facilityToId) {
        this.facilityToId = facilityToId;
    }

    public LongFilter getFacilityLocationToId() {
        return facilityLocationToId;
    }

    public Optional<LongFilter> optionalFacilityLocationToId() {
        return Optional.ofNullable(facilityLocationToId);
    }

    public LongFilter facilityLocationToId() {
        if (facilityLocationToId == null) {
            setFacilityLocationToId(new LongFilter());
        }
        return facilityLocationToId;
    }

    public void setFacilityLocationToId(LongFilter facilityLocationToId) {
        this.facilityLocationToId = facilityLocationToId;
    }

    public LongFilter getIssuanceId() {
        return issuanceId;
    }

    public Optional<LongFilter> optionalIssuanceId() {
        return Optional.ofNullable(issuanceId);
    }

    public LongFilter issuanceId() {
        if (issuanceId == null) {
            setIssuanceId(new LongFilter());
        }
        return issuanceId;
    }

    public void setIssuanceId(LongFilter issuanceId) {
        this.issuanceId = issuanceId;
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
        final InventoryTransferCriteria that = (InventoryTransferCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sentDate, that.sentDate) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(inventoryItemId, that.inventoryItemId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(facilityLocationId, that.facilityLocationId) &&
            Objects.equals(facilityToId, that.facilityToId) &&
            Objects.equals(facilityLocationToId, that.facilityLocationToId) &&
            Objects.equals(issuanceId, that.issuanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sentDate,
            receivedDate,
            comments,
            inventoryItemId,
            statusId,
            facilityId,
            facilityLocationId,
            facilityToId,
            facilityLocationToId,
            issuanceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryTransferCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSentDate().map(f -> "sentDate=" + f + ", ").orElse("") +
            optionalReceivedDate().map(f -> "receivedDate=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalInventoryItemId().map(f -> "inventoryItemId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalFacilityLocationId().map(f -> "facilityLocationId=" + f + ", ").orElse("") +
            optionalFacilityToId().map(f -> "facilityToId=" + f + ", ").orElse("") +
            optionalFacilityLocationToId().map(f -> "facilityLocationToId=" + f + ", ").orElse("") +
            optionalIssuanceId().map(f -> "issuanceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
