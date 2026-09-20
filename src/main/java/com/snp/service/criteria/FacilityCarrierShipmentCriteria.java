package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.FacilityCarrierShipment} entity. This class is used
 * in {@link com.snp.web.rest.FacilityCarrierShipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facility-carrier-shipments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityCarrierShipmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter facilityId;

    private LongFilter partyId;

    private LongFilter roleTypeId;

    private LongFilter shipmentMethodTypeId;

    private Boolean distinct;

    public FacilityCarrierShipmentCriteria() {}

    public FacilityCarrierShipmentCriteria(FacilityCarrierShipmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.shipmentMethodTypeId = other.optionalShipmentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityCarrierShipmentCriteria copy() {
        return new FacilityCarrierShipmentCriteria(this);
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

    public LongFilter getShipmentMethodTypeId() {
        return shipmentMethodTypeId;
    }

    public Optional<LongFilter> optionalShipmentMethodTypeId() {
        return Optional.ofNullable(shipmentMethodTypeId);
    }

    public LongFilter shipmentMethodTypeId() {
        if (shipmentMethodTypeId == null) {
            setShipmentMethodTypeId(new LongFilter());
        }
        return shipmentMethodTypeId;
    }

    public void setShipmentMethodTypeId(LongFilter shipmentMethodTypeId) {
        this.shipmentMethodTypeId = shipmentMethodTypeId;
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
        final FacilityCarrierShipmentCriteria that = (FacilityCarrierShipmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(shipmentMethodTypeId, that.shipmentMethodTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, facilityId, partyId, roleTypeId, shipmentMethodTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityCarrierShipmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalShipmentMethodTypeId().map(f -> "shipmentMethodTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
