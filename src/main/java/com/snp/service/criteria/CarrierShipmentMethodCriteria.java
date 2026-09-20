package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CarrierShipmentMethod} entity. This class is used
 * in {@link com.snp.web.rest.CarrierShipmentMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carrier-shipment-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarrierShipmentMethodCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNo;

    private StringFilter carrierServiceCode;

    private LongFilter shipmentMethodTypeId;

    private LongFilter partyId;

    private LongFilter roleTypeId;

    private Boolean distinct;

    public CarrierShipmentMethodCriteria() {}

    public CarrierShipmentMethodCriteria(CarrierShipmentMethodCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sequenceNo = other.optionalSequenceNo().map(IntegerFilter::copy).orElse(null);
        this.carrierServiceCode = other.optionalCarrierServiceCode().map(StringFilter::copy).orElse(null);
        this.shipmentMethodTypeId = other.optionalShipmentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.partyId = other.optionalPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CarrierShipmentMethodCriteria copy() {
        return new CarrierShipmentMethodCriteria(this);
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

    public IntegerFilter getSequenceNo() {
        return sequenceNo;
    }

    public Optional<IntegerFilter> optionalSequenceNo() {
        return Optional.ofNullable(sequenceNo);
    }

    public IntegerFilter sequenceNo() {
        if (sequenceNo == null) {
            setSequenceNo(new IntegerFilter());
        }
        return sequenceNo;
    }

    public void setSequenceNo(IntegerFilter sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public StringFilter getCarrierServiceCode() {
        return carrierServiceCode;
    }

    public Optional<StringFilter> optionalCarrierServiceCode() {
        return Optional.ofNullable(carrierServiceCode);
    }

    public StringFilter carrierServiceCode() {
        if (carrierServiceCode == null) {
            setCarrierServiceCode(new StringFilter());
        }
        return carrierServiceCode;
    }

    public void setCarrierServiceCode(StringFilter carrierServiceCode) {
        this.carrierServiceCode = carrierServiceCode;
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
        final CarrierShipmentMethodCriteria that = (CarrierShipmentMethodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNo, that.sequenceNo) &&
            Objects.equals(carrierServiceCode, that.carrierServiceCode) &&
            Objects.equals(shipmentMethodTypeId, that.shipmentMethodTypeId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceNo, carrierServiceCode, shipmentMethodTypeId, partyId, roleTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarrierShipmentMethodCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSequenceNo().map(f -> "sequenceNo=" + f + ", ").orElse("") +
            optionalCarrierServiceCode().map(f -> "carrierServiceCode=" + f + ", ").orElse("") +
            optionalShipmentMethodTypeId().map(f -> "shipmentMethodTypeId=" + f + ", ").orElse("") +
            optionalPartyId().map(f -> "partyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
