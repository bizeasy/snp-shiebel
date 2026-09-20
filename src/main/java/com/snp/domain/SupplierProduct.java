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
 * A SupplierProduct.
 */
@Entity
@Table(name = "supplier_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private Instant fromDate;

    @Column(name = "thru_date")
    private Instant thruDate;

    @Size(max = 60)
    @Column(name = "supplier_product_id", length = 60)
    private String supplierProductId;

    @Column(name = "min_order_qty", precision = 21, scale = 2)
    private BigDecimal minOrderQty;

    @Column(name = "last_price", precision = 21, scale = 2)
    private BigDecimal lastPrice;

    @Column(name = "shipping_price", precision = 21, scale = 2)
    private BigDecimal shippingPrice;

    @Column(name = "cgst", precision = 21, scale = 2)
    private BigDecimal cgst;

    @Column(name = "igst", precision = 21, scale = 2)
    private BigDecimal igst;

    @Column(name = "sgst", precision = 21, scale = 2)
    private BigDecimal sgst;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @Size(max = 255)
    @Column(name = "comments", length = 255)
    private String comments;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(name = "units_included", precision = 21, scale = 2)
    private BigDecimal unitsIncluded;

    @Column(name = "can_drop_ship")
    private Boolean canDropShip;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom quantityUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupplierProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public SupplierProduct fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return this.thruDate;
    }

    public SupplierProduct thruDate(Instant thruDate) {
        this.setThruDate(thruDate);
        return this;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public String getSupplierProductId() {
        return this.supplierProductId;
    }

    public SupplierProduct supplierProductId(String supplierProductId) {
        this.setSupplierProductId(supplierProductId);
        return this;
    }

    public void setSupplierProductId(String supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public BigDecimal getMinOrderQty() {
        return this.minOrderQty;
    }

    public SupplierProduct minOrderQty(BigDecimal minOrderQty) {
        this.setMinOrderQty(minOrderQty);
        return this;
    }

    public void setMinOrderQty(BigDecimal minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public BigDecimal getLastPrice() {
        return this.lastPrice;
    }

    public SupplierProduct lastPrice(BigDecimal lastPrice) {
        this.setLastPrice(lastPrice);
        return this;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getShippingPrice() {
        return this.shippingPrice;
    }

    public SupplierProduct shippingPrice(BigDecimal shippingPrice) {
        this.setShippingPrice(shippingPrice);
        return this;
    }

    public void setShippingPrice(BigDecimal shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public BigDecimal getCgst() {
        return this.cgst;
    }

    public SupplierProduct cgst(BigDecimal cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getIgst() {
        return this.igst;
    }

    public SupplierProduct igst(BigDecimal igst) {
        this.setIgst(igst);
        return this;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getSgst() {
        return this.sgst;
    }

    public SupplierProduct sgst(BigDecimal sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public SupplierProduct totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getComments() {
        return this.comments;
    }

    public SupplierProduct comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getLeadTimeDays() {
        return this.leadTimeDays;
    }

    public SupplierProduct leadTimeDays(Integer leadTimeDays) {
        this.setLeadTimeDays(leadTimeDays);
        return this;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public BigDecimal getUnitsIncluded() {
        return this.unitsIncluded;
    }

    public SupplierProduct unitsIncluded(BigDecimal unitsIncluded) {
        this.setUnitsIncluded(unitsIncluded);
        return this;
    }

    public void setUnitsIncluded(BigDecimal unitsIncluded) {
        this.unitsIncluded = unitsIncluded;
    }

    public Boolean getCanDropShip() {
        return this.canDropShip;
    }

    public SupplierProduct canDropShip(Boolean canDropShip) {
        this.setCanDropShip(canDropShip);
        return this;
    }

    public void setCanDropShip(Boolean canDropShip) {
        this.canDropShip = canDropShip;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SupplierProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public SupplierProduct party(Party party) {
        this.setParty(party);
        return this;
    }

    public Uom getQuantityUom() {
        return this.quantityUom;
    }

    public void setQuantityUom(Uom uom) {
        this.quantityUom = uom;
    }

    public SupplierProduct quantityUom(Uom uom) {
        this.setQuantityUom(uom);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public SupplierProduct currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierProduct)) {
            return false;
        }
        return getId() != null && getId().equals(((SupplierProduct) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierProduct{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", thruDate='" + getThruDate() + "'" +
            ", supplierProductId='" + getSupplierProductId() + "'" +
            ", minOrderQty=" + getMinOrderQty() +
            ", lastPrice=" + getLastPrice() +
            ", shippingPrice=" + getShippingPrice() +
            ", cgst=" + getCgst() +
            ", igst=" + getIgst() +
            ", sgst=" + getSgst() +
            ", totalPrice=" + getTotalPrice() +
            ", comments='" + getComments() + "'" +
            ", leadTimeDays=" + getLeadTimeDays() +
            ", unitsIncluded=" + getUnitsIncluded() +
            ", canDropShip='" + getCanDropShip() + "'" +
            "}";
    }
}
