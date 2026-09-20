package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Shipment} entity. This class is used
 * in {@link com.snp.web.rest.ShipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShipmentCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter estimatedShipDate;

    private InstantFilter estimatedReadyDate;

    private InstantFilter estimatedArrivalDate;

    private BigDecimalFilter estimatedShipCost;

    private LongFilter shipmentTypeId;

    private LongFilter shipmentMethodTypeId;

    private LongFilter statusId;

    private LongFilter orderId;

    private LongFilter orderItemId;

    private LongFilter originFacilityId;

    private LongFilter destinationFacilityId;

    private LongFilter fromPartyId;

    private LongFilter toPartyId;

    private LongFilter carrierPartyId;

    private LongFilter contactMechId;

    private Boolean distinct;

    public ShipmentCriteria() {}

    public ShipmentCriteria(ShipmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.estimatedShipDate = other.optionalEstimatedShipDate().map(InstantFilter::copy).orElse(null);
        this.estimatedReadyDate = other.optionalEstimatedReadyDate().map(InstantFilter::copy).orElse(null);
        this.estimatedArrivalDate = other.optionalEstimatedArrivalDate().map(InstantFilter::copy).orElse(null);
        this.estimatedShipCost = other.optionalEstimatedShipCost().map(BigDecimalFilter::copy).orElse(null);
        this.shipmentTypeId = other.optionalShipmentTypeId().map(LongFilter::copy).orElse(null);
        this.shipmentMethodTypeId = other.optionalShipmentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.orderItemId = other.optionalOrderItemId().map(LongFilter::copy).orElse(null);
        this.originFacilityId = other.optionalOriginFacilityId().map(LongFilter::copy).orElse(null);
        this.destinationFacilityId = other.optionalDestinationFacilityId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.toPartyId = other.optionalToPartyId().map(LongFilter::copy).orElse(null);
        this.carrierPartyId = other.optionalCarrierPartyId().map(LongFilter::copy).orElse(null);
        this.contactMechId = other.optionalContactMechId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShipmentCriteria copy() {
        return new ShipmentCriteria(this);
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

    public InstantFilter getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public Optional<InstantFilter> optionalEstimatedShipDate() {
        return Optional.ofNullable(estimatedShipDate);
    }

    public InstantFilter estimatedShipDate() {
        if (estimatedShipDate == null) {
            setEstimatedShipDate(new InstantFilter());
        }
        return estimatedShipDate;
    }

    public void setEstimatedShipDate(InstantFilter estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public InstantFilter getEstimatedReadyDate() {
        return estimatedReadyDate;
    }

    public Optional<InstantFilter> optionalEstimatedReadyDate() {
        return Optional.ofNullable(estimatedReadyDate);
    }

    public InstantFilter estimatedReadyDate() {
        if (estimatedReadyDate == null) {
            setEstimatedReadyDate(new InstantFilter());
        }
        return estimatedReadyDate;
    }

    public void setEstimatedReadyDate(InstantFilter estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    public InstantFilter getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public Optional<InstantFilter> optionalEstimatedArrivalDate() {
        return Optional.ofNullable(estimatedArrivalDate);
    }

    public InstantFilter estimatedArrivalDate() {
        if (estimatedArrivalDate == null) {
            setEstimatedArrivalDate(new InstantFilter());
        }
        return estimatedArrivalDate;
    }

    public void setEstimatedArrivalDate(InstantFilter estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public BigDecimalFilter getEstimatedShipCost() {
        return estimatedShipCost;
    }

    public Optional<BigDecimalFilter> optionalEstimatedShipCost() {
        return Optional.ofNullable(estimatedShipCost);
    }

    public BigDecimalFilter estimatedShipCost() {
        if (estimatedShipCost == null) {
            setEstimatedShipCost(new BigDecimalFilter());
        }
        return estimatedShipCost;
    }

    public void setEstimatedShipCost(BigDecimalFilter estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
    }

    public LongFilter getShipmentTypeId() {
        return shipmentTypeId;
    }

    public Optional<LongFilter> optionalShipmentTypeId() {
        return Optional.ofNullable(shipmentTypeId);
    }

    public LongFilter shipmentTypeId() {
        if (shipmentTypeId == null) {
            setShipmentTypeId(new LongFilter());
        }
        return shipmentTypeId;
    }

    public void setShipmentTypeId(LongFilter shipmentTypeId) {
        this.shipmentTypeId = shipmentTypeId;
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

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public Optional<LongFilter> optionalOrderItemId() {
        return Optional.ofNullable(orderItemId);
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            setOrderItemId(new LongFilter());
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getOriginFacilityId() {
        return originFacilityId;
    }

    public Optional<LongFilter> optionalOriginFacilityId() {
        return Optional.ofNullable(originFacilityId);
    }

    public LongFilter originFacilityId() {
        if (originFacilityId == null) {
            setOriginFacilityId(new LongFilter());
        }
        return originFacilityId;
    }

    public void setOriginFacilityId(LongFilter originFacilityId) {
        this.originFacilityId = originFacilityId;
    }

    public LongFilter getDestinationFacilityId() {
        return destinationFacilityId;
    }

    public Optional<LongFilter> optionalDestinationFacilityId() {
        return Optional.ofNullable(destinationFacilityId);
    }

    public LongFilter destinationFacilityId() {
        if (destinationFacilityId == null) {
            setDestinationFacilityId(new LongFilter());
        }
        return destinationFacilityId;
    }

    public void setDestinationFacilityId(LongFilter destinationFacilityId) {
        this.destinationFacilityId = destinationFacilityId;
    }

    public LongFilter getFromPartyId() {
        return fromPartyId;
    }

    public Optional<LongFilter> optionalFromPartyId() {
        return Optional.ofNullable(fromPartyId);
    }

    public LongFilter fromPartyId() {
        if (fromPartyId == null) {
            setFromPartyId(new LongFilter());
        }
        return fromPartyId;
    }

    public void setFromPartyId(LongFilter fromPartyId) {
        this.fromPartyId = fromPartyId;
    }

    public LongFilter getToPartyId() {
        return toPartyId;
    }

    public Optional<LongFilter> optionalToPartyId() {
        return Optional.ofNullable(toPartyId);
    }

    public LongFilter toPartyId() {
        if (toPartyId == null) {
            setToPartyId(new LongFilter());
        }
        return toPartyId;
    }

    public void setToPartyId(LongFilter toPartyId) {
        this.toPartyId = toPartyId;
    }

    public LongFilter getCarrierPartyId() {
        return carrierPartyId;
    }

    public Optional<LongFilter> optionalCarrierPartyId() {
        return Optional.ofNullable(carrierPartyId);
    }

    public LongFilter carrierPartyId() {
        if (carrierPartyId == null) {
            setCarrierPartyId(new LongFilter());
        }
        return carrierPartyId;
    }

    public void setCarrierPartyId(LongFilter carrierPartyId) {
        this.carrierPartyId = carrierPartyId;
    }

    public LongFilter getContactMechId() {
        return contactMechId;
    }

    public Optional<LongFilter> optionalContactMechId() {
        return Optional.ofNullable(contactMechId);
    }

    public LongFilter contactMechId() {
        if (contactMechId == null) {
            setContactMechId(new LongFilter());
        }
        return contactMechId;
    }

    public void setContactMechId(LongFilter contactMechId) {
        this.contactMechId = contactMechId;
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
        final ShipmentCriteria that = (ShipmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(estimatedShipDate, that.estimatedShipDate) &&
            Objects.equals(estimatedReadyDate, that.estimatedReadyDate) &&
            Objects.equals(estimatedArrivalDate, that.estimatedArrivalDate) &&
            Objects.equals(estimatedShipCost, that.estimatedShipCost) &&
            Objects.equals(shipmentTypeId, that.shipmentTypeId) &&
            Objects.equals(shipmentMethodTypeId, that.shipmentMethodTypeId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(originFacilityId, that.originFacilityId) &&
            Objects.equals(destinationFacilityId, that.destinationFacilityId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(toPartyId, that.toPartyId) &&
            Objects.equals(carrierPartyId, that.carrierPartyId) &&
            Objects.equals(contactMechId, that.contactMechId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            estimatedShipDate,
            estimatedReadyDate,
            estimatedArrivalDate,
            estimatedShipCost,
            shipmentTypeId,
            shipmentMethodTypeId,
            statusId,
            orderId,
            orderItemId,
            originFacilityId,
            destinationFacilityId,
            fromPartyId,
            toPartyId,
            carrierPartyId,
            contactMechId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEstimatedShipDate().map(f -> "estimatedShipDate=" + f + ", ").orElse("") +
            optionalEstimatedReadyDate().map(f -> "estimatedReadyDate=" + f + ", ").orElse("") +
            optionalEstimatedArrivalDate().map(f -> "estimatedArrivalDate=" + f + ", ").orElse("") +
            optionalEstimatedShipCost().map(f -> "estimatedShipCost=" + f + ", ").orElse("") +
            optionalShipmentTypeId().map(f -> "shipmentTypeId=" + f + ", ").orElse("") +
            optionalShipmentMethodTypeId().map(f -> "shipmentMethodTypeId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderItemId().map(f -> "orderItemId=" + f + ", ").orElse("") +
            optionalOriginFacilityId().map(f -> "originFacilityId=" + f + ", ").orElse("") +
            optionalDestinationFacilityId().map(f -> "destinationFacilityId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalToPartyId().map(f -> "toPartyId=" + f + ", ").orElse("") +
            optionalCarrierPartyId().map(f -> "carrierPartyId=" + f + ", ").orElse("") +
            optionalContactMechId().map(f -> "contactMechId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
