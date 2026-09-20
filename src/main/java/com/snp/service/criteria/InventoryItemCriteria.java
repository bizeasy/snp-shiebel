package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.InventoryItem} entity. This class is used
 * in {@link com.snp.web.rest.InventoryItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventory-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter receivedDate;

    private InstantFilter manufacturedDate;

    private InstantFilter expiryDate;

    private InstantFilter retestDate;

    private StringFilter containerId;

    private StringFilter batchNo;

    private StringFilter mfgBatchNo;

    private StringFilter lotNoStr;

    private StringFilter binNumber;

    private StringFilter comments;

    private BigDecimalFilter quantityOnHandTotal;

    private BigDecimalFilter availableToPromiseTotal;

    private BigDecimalFilter accountingQuantityTotal;

    private BigDecimalFilter oldQuantityOnHand;

    private BigDecimalFilter oldAvailableToPromise;

    private StringFilter serialNumber;

    private StringFilter softIdentifier;

    private StringFilter activationNumber;

    private InstantFilter activationValidThru;

    private BigDecimalFilter unitCost;

    private LongFilter inventoryItemTypeId;

    private LongFilter productId;

    private LongFilter supplierId;

    private LongFilter ownerPartyId;

    private LongFilter statusId;

    private LongFilter facilityId;

    private LongFilter facilityLocationId;

    private LongFilter uomId;

    private LongFilter currencyUomId;

    private LongFilter lotId;

    private Boolean distinct;

    public InventoryItemCriteria() {}

    public InventoryItemCriteria(InventoryItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.receivedDate = other.optionalReceivedDate().map(InstantFilter::copy).orElse(null);
        this.manufacturedDate = other.optionalManufacturedDate().map(InstantFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(InstantFilter::copy).orElse(null);
        this.retestDate = other.optionalRetestDate().map(InstantFilter::copy).orElse(null);
        this.containerId = other.optionalContainerId().map(StringFilter::copy).orElse(null);
        this.batchNo = other.optionalBatchNo().map(StringFilter::copy).orElse(null);
        this.mfgBatchNo = other.optionalMfgBatchNo().map(StringFilter::copy).orElse(null);
        this.lotNoStr = other.optionalLotNoStr().map(StringFilter::copy).orElse(null);
        this.binNumber = other.optionalBinNumber().map(StringFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.quantityOnHandTotal = other.optionalQuantityOnHandTotal().map(BigDecimalFilter::copy).orElse(null);
        this.availableToPromiseTotal = other.optionalAvailableToPromiseTotal().map(BigDecimalFilter::copy).orElse(null);
        this.accountingQuantityTotal = other.optionalAccountingQuantityTotal().map(BigDecimalFilter::copy).orElse(null);
        this.oldQuantityOnHand = other.optionalOldQuantityOnHand().map(BigDecimalFilter::copy).orElse(null);
        this.oldAvailableToPromise = other.optionalOldAvailableToPromise().map(BigDecimalFilter::copy).orElse(null);
        this.serialNumber = other.optionalSerialNumber().map(StringFilter::copy).orElse(null);
        this.softIdentifier = other.optionalSoftIdentifier().map(StringFilter::copy).orElse(null);
        this.activationNumber = other.optionalActivationNumber().map(StringFilter::copy).orElse(null);
        this.activationValidThru = other.optionalActivationValidThru().map(InstantFilter::copy).orElse(null);
        this.unitCost = other.optionalUnitCost().map(BigDecimalFilter::copy).orElse(null);
        this.inventoryItemTypeId = other.optionalInventoryItemTypeId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(LongFilter::copy).orElse(null);
        this.ownerPartyId = other.optionalOwnerPartyId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.facilityId = other.optionalFacilityId().map(LongFilter::copy).orElse(null);
        this.facilityLocationId = other.optionalFacilityLocationId().map(LongFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.lotId = other.optionalLotId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryItemCriteria copy() {
        return new InventoryItemCriteria(this);
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

    public InstantFilter getManufacturedDate() {
        return manufacturedDate;
    }

    public Optional<InstantFilter> optionalManufacturedDate() {
        return Optional.ofNullable(manufacturedDate);
    }

    public InstantFilter manufacturedDate() {
        if (manufacturedDate == null) {
            setManufacturedDate(new InstantFilter());
        }
        return manufacturedDate;
    }

    public void setManufacturedDate(InstantFilter manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
    }

    public InstantFilter getExpiryDate() {
        return expiryDate;
    }

    public Optional<InstantFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public InstantFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new InstantFilter());
        }
        return expiryDate;
    }

    public void setExpiryDate(InstantFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public InstantFilter getRetestDate() {
        return retestDate;
    }

    public Optional<InstantFilter> optionalRetestDate() {
        return Optional.ofNullable(retestDate);
    }

    public InstantFilter retestDate() {
        if (retestDate == null) {
            setRetestDate(new InstantFilter());
        }
        return retestDate;
    }

    public void setRetestDate(InstantFilter retestDate) {
        this.retestDate = retestDate;
    }

    public StringFilter getContainerId() {
        return containerId;
    }

    public Optional<StringFilter> optionalContainerId() {
        return Optional.ofNullable(containerId);
    }

    public StringFilter containerId() {
        if (containerId == null) {
            setContainerId(new StringFilter());
        }
        return containerId;
    }

    public void setContainerId(StringFilter containerId) {
        this.containerId = containerId;
    }

    public StringFilter getBatchNo() {
        return batchNo;
    }

    public Optional<StringFilter> optionalBatchNo() {
        return Optional.ofNullable(batchNo);
    }

    public StringFilter batchNo() {
        if (batchNo == null) {
            setBatchNo(new StringFilter());
        }
        return batchNo;
    }

    public void setBatchNo(StringFilter batchNo) {
        this.batchNo = batchNo;
    }

    public StringFilter getMfgBatchNo() {
        return mfgBatchNo;
    }

    public Optional<StringFilter> optionalMfgBatchNo() {
        return Optional.ofNullable(mfgBatchNo);
    }

    public StringFilter mfgBatchNo() {
        if (mfgBatchNo == null) {
            setMfgBatchNo(new StringFilter());
        }
        return mfgBatchNo;
    }

    public void setMfgBatchNo(StringFilter mfgBatchNo) {
        this.mfgBatchNo = mfgBatchNo;
    }

    public StringFilter getLotNoStr() {
        return lotNoStr;
    }

    public Optional<StringFilter> optionalLotNoStr() {
        return Optional.ofNullable(lotNoStr);
    }

    public StringFilter lotNoStr() {
        if (lotNoStr == null) {
            setLotNoStr(new StringFilter());
        }
        return lotNoStr;
    }

    public void setLotNoStr(StringFilter lotNoStr) {
        this.lotNoStr = lotNoStr;
    }

    public StringFilter getBinNumber() {
        return binNumber;
    }

    public Optional<StringFilter> optionalBinNumber() {
        return Optional.ofNullable(binNumber);
    }

    public StringFilter binNumber() {
        if (binNumber == null) {
            setBinNumber(new StringFilter());
        }
        return binNumber;
    }

    public void setBinNumber(StringFilter binNumber) {
        this.binNumber = binNumber;
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

    public BigDecimalFilter getQuantityOnHandTotal() {
        return quantityOnHandTotal;
    }

    public Optional<BigDecimalFilter> optionalQuantityOnHandTotal() {
        return Optional.ofNullable(quantityOnHandTotal);
    }

    public BigDecimalFilter quantityOnHandTotal() {
        if (quantityOnHandTotal == null) {
            setQuantityOnHandTotal(new BigDecimalFilter());
        }
        return quantityOnHandTotal;
    }

    public void setQuantityOnHandTotal(BigDecimalFilter quantityOnHandTotal) {
        this.quantityOnHandTotal = quantityOnHandTotal;
    }

    public BigDecimalFilter getAvailableToPromiseTotal() {
        return availableToPromiseTotal;
    }

    public Optional<BigDecimalFilter> optionalAvailableToPromiseTotal() {
        return Optional.ofNullable(availableToPromiseTotal);
    }

    public BigDecimalFilter availableToPromiseTotal() {
        if (availableToPromiseTotal == null) {
            setAvailableToPromiseTotal(new BigDecimalFilter());
        }
        return availableToPromiseTotal;
    }

    public void setAvailableToPromiseTotal(BigDecimalFilter availableToPromiseTotal) {
        this.availableToPromiseTotal = availableToPromiseTotal;
    }

    public BigDecimalFilter getAccountingQuantityTotal() {
        return accountingQuantityTotal;
    }

    public Optional<BigDecimalFilter> optionalAccountingQuantityTotal() {
        return Optional.ofNullable(accountingQuantityTotal);
    }

    public BigDecimalFilter accountingQuantityTotal() {
        if (accountingQuantityTotal == null) {
            setAccountingQuantityTotal(new BigDecimalFilter());
        }
        return accountingQuantityTotal;
    }

    public void setAccountingQuantityTotal(BigDecimalFilter accountingQuantityTotal) {
        this.accountingQuantityTotal = accountingQuantityTotal;
    }

    public BigDecimalFilter getOldQuantityOnHand() {
        return oldQuantityOnHand;
    }

    public Optional<BigDecimalFilter> optionalOldQuantityOnHand() {
        return Optional.ofNullable(oldQuantityOnHand);
    }

    public BigDecimalFilter oldQuantityOnHand() {
        if (oldQuantityOnHand == null) {
            setOldQuantityOnHand(new BigDecimalFilter());
        }
        return oldQuantityOnHand;
    }

    public void setOldQuantityOnHand(BigDecimalFilter oldQuantityOnHand) {
        this.oldQuantityOnHand = oldQuantityOnHand;
    }

    public BigDecimalFilter getOldAvailableToPromise() {
        return oldAvailableToPromise;
    }

    public Optional<BigDecimalFilter> optionalOldAvailableToPromise() {
        return Optional.ofNullable(oldAvailableToPromise);
    }

    public BigDecimalFilter oldAvailableToPromise() {
        if (oldAvailableToPromise == null) {
            setOldAvailableToPromise(new BigDecimalFilter());
        }
        return oldAvailableToPromise;
    }

    public void setOldAvailableToPromise(BigDecimalFilter oldAvailableToPromise) {
        this.oldAvailableToPromise = oldAvailableToPromise;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public Optional<StringFilter> optionalSerialNumber() {
        return Optional.ofNullable(serialNumber);
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            setSerialNumber(new StringFilter());
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getSoftIdentifier() {
        return softIdentifier;
    }

    public Optional<StringFilter> optionalSoftIdentifier() {
        return Optional.ofNullable(softIdentifier);
    }

    public StringFilter softIdentifier() {
        if (softIdentifier == null) {
            setSoftIdentifier(new StringFilter());
        }
        return softIdentifier;
    }

    public void setSoftIdentifier(StringFilter softIdentifier) {
        this.softIdentifier = softIdentifier;
    }

    public StringFilter getActivationNumber() {
        return activationNumber;
    }

    public Optional<StringFilter> optionalActivationNumber() {
        return Optional.ofNullable(activationNumber);
    }

    public StringFilter activationNumber() {
        if (activationNumber == null) {
            setActivationNumber(new StringFilter());
        }
        return activationNumber;
    }

    public void setActivationNumber(StringFilter activationNumber) {
        this.activationNumber = activationNumber;
    }

    public InstantFilter getActivationValidThru() {
        return activationValidThru;
    }

    public Optional<InstantFilter> optionalActivationValidThru() {
        return Optional.ofNullable(activationValidThru);
    }

    public InstantFilter activationValidThru() {
        if (activationValidThru == null) {
            setActivationValidThru(new InstantFilter());
        }
        return activationValidThru;
    }

    public void setActivationValidThru(InstantFilter activationValidThru) {
        this.activationValidThru = activationValidThru;
    }

    public BigDecimalFilter getUnitCost() {
        return unitCost;
    }

    public Optional<BigDecimalFilter> optionalUnitCost() {
        return Optional.ofNullable(unitCost);
    }

    public BigDecimalFilter unitCost() {
        if (unitCost == null) {
            setUnitCost(new BigDecimalFilter());
        }
        return unitCost;
    }

    public void setUnitCost(BigDecimalFilter unitCost) {
        this.unitCost = unitCost;
    }

    public LongFilter getInventoryItemTypeId() {
        return inventoryItemTypeId;
    }

    public Optional<LongFilter> optionalInventoryItemTypeId() {
        return Optional.ofNullable(inventoryItemTypeId);
    }

    public LongFilter inventoryItemTypeId() {
        if (inventoryItemTypeId == null) {
            setInventoryItemTypeId(new LongFilter());
        }
        return inventoryItemTypeId;
    }

    public void setInventoryItemTypeId(LongFilter inventoryItemTypeId) {
        this.inventoryItemTypeId = inventoryItemTypeId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public Optional<LongFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new LongFilter());
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public LongFilter getOwnerPartyId() {
        return ownerPartyId;
    }

    public Optional<LongFilter> optionalOwnerPartyId() {
        return Optional.ofNullable(ownerPartyId);
    }

    public LongFilter ownerPartyId() {
        if (ownerPartyId == null) {
            setOwnerPartyId(new LongFilter());
        }
        return ownerPartyId;
    }

    public void setOwnerPartyId(LongFilter ownerPartyId) {
        this.ownerPartyId = ownerPartyId;
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

    public LongFilter getUomId() {
        return uomId;
    }

    public Optional<LongFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public LongFilter uomId() {
        if (uomId == null) {
            setUomId(new LongFilter());
        }
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public LongFilter getCurrencyUomId() {
        return currencyUomId;
    }

    public Optional<LongFilter> optionalCurrencyUomId() {
        return Optional.ofNullable(currencyUomId);
    }

    public LongFilter currencyUomId() {
        if (currencyUomId == null) {
            setCurrencyUomId(new LongFilter());
        }
        return currencyUomId;
    }

    public void setCurrencyUomId(LongFilter currencyUomId) {
        this.currencyUomId = currencyUomId;
    }

    public LongFilter getLotId() {
        return lotId;
    }

    public Optional<LongFilter> optionalLotId() {
        return Optional.ofNullable(lotId);
    }

    public LongFilter lotId() {
        if (lotId == null) {
            setLotId(new LongFilter());
        }
        return lotId;
    }

    public void setLotId(LongFilter lotId) {
        this.lotId = lotId;
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
        final InventoryItemCriteria that = (InventoryItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(receivedDate, that.receivedDate) &&
            Objects.equals(manufacturedDate, that.manufacturedDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(retestDate, that.retestDate) &&
            Objects.equals(containerId, that.containerId) &&
            Objects.equals(batchNo, that.batchNo) &&
            Objects.equals(mfgBatchNo, that.mfgBatchNo) &&
            Objects.equals(lotNoStr, that.lotNoStr) &&
            Objects.equals(binNumber, that.binNumber) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(quantityOnHandTotal, that.quantityOnHandTotal) &&
            Objects.equals(availableToPromiseTotal, that.availableToPromiseTotal) &&
            Objects.equals(accountingQuantityTotal, that.accountingQuantityTotal) &&
            Objects.equals(oldQuantityOnHand, that.oldQuantityOnHand) &&
            Objects.equals(oldAvailableToPromise, that.oldAvailableToPromise) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(softIdentifier, that.softIdentifier) &&
            Objects.equals(activationNumber, that.activationNumber) &&
            Objects.equals(activationValidThru, that.activationValidThru) &&
            Objects.equals(unitCost, that.unitCost) &&
            Objects.equals(inventoryItemTypeId, that.inventoryItemTypeId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(ownerPartyId, that.ownerPartyId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(facilityId, that.facilityId) &&
            Objects.equals(facilityLocationId, that.facilityLocationId) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(lotId, that.lotId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            receivedDate,
            manufacturedDate,
            expiryDate,
            retestDate,
            containerId,
            batchNo,
            mfgBatchNo,
            lotNoStr,
            binNumber,
            comments,
            quantityOnHandTotal,
            availableToPromiseTotal,
            accountingQuantityTotal,
            oldQuantityOnHand,
            oldAvailableToPromise,
            serialNumber,
            softIdentifier,
            activationNumber,
            activationValidThru,
            unitCost,
            inventoryItemTypeId,
            productId,
            supplierId,
            ownerPartyId,
            statusId,
            facilityId,
            facilityLocationId,
            uomId,
            currencyUomId,
            lotId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReceivedDate().map(f -> "receivedDate=" + f + ", ").orElse("") +
            optionalManufacturedDate().map(f -> "manufacturedDate=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalRetestDate().map(f -> "retestDate=" + f + ", ").orElse("") +
            optionalContainerId().map(f -> "containerId=" + f + ", ").orElse("") +
            optionalBatchNo().map(f -> "batchNo=" + f + ", ").orElse("") +
            optionalMfgBatchNo().map(f -> "mfgBatchNo=" + f + ", ").orElse("") +
            optionalLotNoStr().map(f -> "lotNoStr=" + f + ", ").orElse("") +
            optionalBinNumber().map(f -> "binNumber=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalQuantityOnHandTotal().map(f -> "quantityOnHandTotal=" + f + ", ").orElse("") +
            optionalAvailableToPromiseTotal().map(f -> "availableToPromiseTotal=" + f + ", ").orElse("") +
            optionalAccountingQuantityTotal().map(f -> "accountingQuantityTotal=" + f + ", ").orElse("") +
            optionalOldQuantityOnHand().map(f -> "oldQuantityOnHand=" + f + ", ").orElse("") +
            optionalOldAvailableToPromise().map(f -> "oldAvailableToPromise=" + f + ", ").orElse("") +
            optionalSerialNumber().map(f -> "serialNumber=" + f + ", ").orElse("") +
            optionalSoftIdentifier().map(f -> "softIdentifier=" + f + ", ").orElse("") +
            optionalActivationNumber().map(f -> "activationNumber=" + f + ", ").orElse("") +
            optionalActivationValidThru().map(f -> "activationValidThru=" + f + ", ").orElse("") +
            optionalUnitCost().map(f -> "unitCost=" + f + ", ").orElse("") +
            optionalInventoryItemTypeId().map(f -> "inventoryItemTypeId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalOwnerPartyId().map(f -> "ownerPartyId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalFacilityId().map(f -> "facilityId=" + f + ", ").orElse("") +
            optionalFacilityLocationId().map(f -> "facilityLocationId=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalLotId().map(f -> "lotId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
