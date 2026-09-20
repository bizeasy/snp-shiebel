package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InventoryItem.
 */
@Entity
@Table(name = "inventory_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Column(name = "manufactured_date")
    private Instant manufacturedDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "retest_date")
    private Instant retestDate;

    @Size(max = 60)
    @Column(name = "container_id", length = 60)
    private String containerId;

    @Size(max = 60)
    @Column(name = "batch_no", length = 60)
    private String batchNo;

    @Size(max = 60)
    @Column(name = "mfg_batch_no", length = 60)
    private String mfgBatchNo;

    @Size(max = 60)
    @Column(name = "lot_no_str", length = 60)
    private String lotNoStr;

    @Size(max = 60)
    @Column(name = "bin_number", length = 60)
    private String binNumber;

    @Size(max = 255)
    @Column(name = "comments", length = 255)
    private String comments;

    @Column(name = "quantity_on_hand_total", precision = 21, scale = 2)
    private BigDecimal quantityOnHandTotal;

    @Column(name = "available_to_promise_total", precision = 21, scale = 2)
    private BigDecimal availableToPromiseTotal;

    @Column(name = "accounting_quantity_total", precision = 21, scale = 2)
    private BigDecimal accountingQuantityTotal;

    @Column(name = "old_quantity_on_hand", precision = 21, scale = 2)
    private BigDecimal oldQuantityOnHand;

    @Column(name = "old_available_to_promise", precision = 21, scale = 2)
    private BigDecimal oldAvailableToPromise;

    @Size(max = 255)
    @Column(name = "serial_number", length = 255)
    private String serialNumber;

    @Size(max = 255)
    @Column(name = "soft_identifier", length = 255)
    private String softIdentifier;

    @Size(max = 255)
    @Column(name = "activation_number", length = 255)
    private String activationNumber;

    @Column(name = "activation_valid_thru")
    private Instant activationValidThru;

    @Column(name = "unit_cost", precision = 21, scale = 2)
    private BigDecimal unitCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private InventoryItemType inventoryItemType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "productType",
            "primaryProductCategory",
            "virtualProduct",
            "inventoryItemType",
            "taxSlab",
            "uom",
            "quantityUom",
            "weightUom",
            "sizeUom",
        },
        allowSetters = true
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party ownerParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "facilityType",
            "parentFacility",
            "ownerParty",
            "facilityGroup",
            "sizeUom",
            "geoPoint",
            "inventoryItemType",
            "productStore",
        },
        allowSetters = true
    )
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "facility", "locationType" }, allowSetters = true)
    private FacilityLocation facilityLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lot lot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public InventoryItem receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Instant getManufacturedDate() {
        return this.manufacturedDate;
    }

    public InventoryItem manufacturedDate(Instant manufacturedDate) {
        this.setManufacturedDate(manufacturedDate);
        return this;
    }

    public void setManufacturedDate(Instant manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public InventoryItem expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Instant getRetestDate() {
        return this.retestDate;
    }

    public InventoryItem retestDate(Instant retestDate) {
        this.setRetestDate(retestDate);
        return this;
    }

    public void setRetestDate(Instant retestDate) {
        this.retestDate = retestDate;
    }

    public String getContainerId() {
        return this.containerId;
    }

    public InventoryItem containerId(String containerId) {
        this.setContainerId(containerId);
        return this;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public InventoryItem batchNo(String batchNo) {
        this.setBatchNo(batchNo);
        return this;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMfgBatchNo() {
        return this.mfgBatchNo;
    }

    public InventoryItem mfgBatchNo(String mfgBatchNo) {
        this.setMfgBatchNo(mfgBatchNo);
        return this;
    }

    public void setMfgBatchNo(String mfgBatchNo) {
        this.mfgBatchNo = mfgBatchNo;
    }

    public String getLotNoStr() {
        return this.lotNoStr;
    }

    public InventoryItem lotNoStr(String lotNoStr) {
        this.setLotNoStr(lotNoStr);
        return this;
    }

    public void setLotNoStr(String lotNoStr) {
        this.lotNoStr = lotNoStr;
    }

    public String getBinNumber() {
        return this.binNumber;
    }

    public InventoryItem binNumber(String binNumber) {
        this.setBinNumber(binNumber);
        return this;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public String getComments() {
        return this.comments;
    }

    public InventoryItem comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getQuantityOnHandTotal() {
        return this.quantityOnHandTotal;
    }

    public InventoryItem quantityOnHandTotal(BigDecimal quantityOnHandTotal) {
        this.setQuantityOnHandTotal(quantityOnHandTotal);
        return this;
    }

    public void setQuantityOnHandTotal(BigDecimal quantityOnHandTotal) {
        this.quantityOnHandTotal = quantityOnHandTotal;
    }

    public BigDecimal getAvailableToPromiseTotal() {
        return this.availableToPromiseTotal;
    }

    public InventoryItem availableToPromiseTotal(BigDecimal availableToPromiseTotal) {
        this.setAvailableToPromiseTotal(availableToPromiseTotal);
        return this;
    }

    public void setAvailableToPromiseTotal(BigDecimal availableToPromiseTotal) {
        this.availableToPromiseTotal = availableToPromiseTotal;
    }

    public BigDecimal getAccountingQuantityTotal() {
        return this.accountingQuantityTotal;
    }

    public InventoryItem accountingQuantityTotal(BigDecimal accountingQuantityTotal) {
        this.setAccountingQuantityTotal(accountingQuantityTotal);
        return this;
    }

    public void setAccountingQuantityTotal(BigDecimal accountingQuantityTotal) {
        this.accountingQuantityTotal = accountingQuantityTotal;
    }

    public BigDecimal getOldQuantityOnHand() {
        return this.oldQuantityOnHand;
    }

    public InventoryItem oldQuantityOnHand(BigDecimal oldQuantityOnHand) {
        this.setOldQuantityOnHand(oldQuantityOnHand);
        return this;
    }

    public void setOldQuantityOnHand(BigDecimal oldQuantityOnHand) {
        this.oldQuantityOnHand = oldQuantityOnHand;
    }

    public BigDecimal getOldAvailableToPromise() {
        return this.oldAvailableToPromise;
    }

    public InventoryItem oldAvailableToPromise(BigDecimal oldAvailableToPromise) {
        this.setOldAvailableToPromise(oldAvailableToPromise);
        return this;
    }

    public void setOldAvailableToPromise(BigDecimal oldAvailableToPromise) {
        this.oldAvailableToPromise = oldAvailableToPromise;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public InventoryItem serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftIdentifier() {
        return this.softIdentifier;
    }

    public InventoryItem softIdentifier(String softIdentifier) {
        this.setSoftIdentifier(softIdentifier);
        return this;
    }

    public void setSoftIdentifier(String softIdentifier) {
        this.softIdentifier = softIdentifier;
    }

    public String getActivationNumber() {
        return this.activationNumber;
    }

    public InventoryItem activationNumber(String activationNumber) {
        this.setActivationNumber(activationNumber);
        return this;
    }

    public void setActivationNumber(String activationNumber) {
        this.activationNumber = activationNumber;
    }

    public Instant getActivationValidThru() {
        return this.activationValidThru;
    }

    public InventoryItem activationValidThru(Instant activationValidThru) {
        this.setActivationValidThru(activationValidThru);
        return this;
    }

    public void setActivationValidThru(Instant activationValidThru) {
        this.activationValidThru = activationValidThru;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public InventoryItem unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public InventoryItemType getInventoryItemType() {
        return this.inventoryItemType;
    }

    public void setInventoryItemType(InventoryItemType inventoryItemType) {
        this.inventoryItemType = inventoryItemType;
    }

    public InventoryItem inventoryItemType(InventoryItemType inventoryItemType) {
        this.setInventoryItemType(inventoryItemType);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InventoryItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Party getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Party party) {
        this.supplier = party;
    }

    public InventoryItem supplier(Party party) {
        this.setSupplier(party);
        return this;
    }

    public Party getOwnerParty() {
        return this.ownerParty;
    }

    public void setOwnerParty(Party party) {
        this.ownerParty = party;
    }

    public InventoryItem ownerParty(Party party) {
        this.setOwnerParty(party);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public InventoryItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public InventoryItem facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    public FacilityLocation getFacilityLocation() {
        return this.facilityLocation;
    }

    public void setFacilityLocation(FacilityLocation facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public InventoryItem facilityLocation(FacilityLocation facilityLocation) {
        this.setFacilityLocation(facilityLocation);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public InventoryItem uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public InventoryItem currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    public Lot getLot() {
        return this.lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public InventoryItem lot(Lot lot) {
        this.setLot(lot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItem)) {
            return false;
        }
        return getId() != null && getId().equals(((InventoryItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItem{" +
            "id=" + getId() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", manufacturedDate='" + getManufacturedDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", retestDate='" + getRetestDate() + "'" +
            ", containerId='" + getContainerId() + "'" +
            ", batchNo='" + getBatchNo() + "'" +
            ", mfgBatchNo='" + getMfgBatchNo() + "'" +
            ", lotNoStr='" + getLotNoStr() + "'" +
            ", binNumber='" + getBinNumber() + "'" +
            ", comments='" + getComments() + "'" +
            ", quantityOnHandTotal=" + getQuantityOnHandTotal() +
            ", availableToPromiseTotal=" + getAvailableToPromiseTotal() +
            ", accountingQuantityTotal=" + getAccountingQuantityTotal() +
            ", oldQuantityOnHand=" + getOldQuantityOnHand() +
            ", oldAvailableToPromise=" + getOldAvailableToPromise() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", softIdentifier='" + getSoftIdentifier() + "'" +
            ", activationNumber='" + getActivationNumber() + "'" +
            ", activationValidThru='" + getActivationValidThru() + "'" +
            ", unitCost=" + getUnitCost() +
            "}";
    }
}
