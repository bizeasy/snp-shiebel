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
 * A InvoiceItem.
 */
@Entity
@Table(name = "invoice_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "cancel_quantity", precision = 21, scale = 2)
    private BigDecimal cancelQuantity;

    @Column(name = "selected_amount", precision = 21, scale = 2)
    private BigDecimal selectedAmount;

    @Column(name = "unit_price", precision = 21, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unit_list_price", precision = 21, scale = 2)
    private BigDecimal unitListPrice;

    @Column(name = "is_modified_price")
    private Boolean isModifiedPrice;

    @Column(name = "estimated_ship_date")
    private Instant estimatedShipDate;

    @Column(name = "estimated_delivery_date")
    private Instant estimatedDeliveryDate;

    @Column(name = "ship_before_date")
    private Instant shipBeforeDate;

    @Column(name = "ship_after_date")
    private Instant shipAfterDate;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Size(max = 255)
    @Column(name = "attr_1", length = 255)
    private String attr1;

    @Size(max = 255)
    @Column(name = "attr_2", length = 255)
    private String attr2;

    @Size(max = 255)
    @Column(name = "attr_3", length = 255)
    private String attr3;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "invoiceType", "order", "fromParty", "toParty", "roleType", "status", "contactMech", "currencyUom" },
        allowSetters = true
    )
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    private InvoiceItemType invoiceItemType;

    @ManyToOne(fetch = FetchType.LAZY)
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
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productFeatureType", "productFeatureCategory" }, allowSetters = true)
    private ProductFeature productFeature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "product", "facility" }, allowSetters = true)
    private ProductFacility productFacility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "inventoryItemType",
            "product",
            "supplier",
            "ownerParty",
            "status",
            "facility",
            "facilityLocation",
            "uom",
            "currencyUom",
            "lot",
        },
        allowSetters = true
    )
    private InventoryItem fromInventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "taxAuthority", "taxSlab" }, allowSetters = true)
    private TaxAuthorityRateType taxAuthRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "invoiceType", "order", "fromParty", "toParty", "roleType", "status", "contactMech", "currencyUom" },
        allowSetters = true
    )
    private Invoice parentInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "invoice",
            "invoiceItemType",
            "product",
            "uom",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "taxAuthRate",
            "parentInvoice",
            "parentInvoiceItem",
        },
        allowSetters = true
    )
    private InvoiceItem parentInvoiceItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public InvoiceItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public InvoiceItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public InvoiceItem amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCancelQuantity() {
        return this.cancelQuantity;
    }

    public InvoiceItem cancelQuantity(BigDecimal cancelQuantity) {
        this.setCancelQuantity(cancelQuantity);
        return this;
    }

    public void setCancelQuantity(BigDecimal cancelQuantity) {
        this.cancelQuantity = cancelQuantity;
    }

    public BigDecimal getSelectedAmount() {
        return this.selectedAmount;
    }

    public InvoiceItem selectedAmount(BigDecimal selectedAmount) {
        this.setSelectedAmount(selectedAmount);
        return this;
    }

    public void setSelectedAmount(BigDecimal selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public InvoiceItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitListPrice() {
        return this.unitListPrice;
    }

    public InvoiceItem unitListPrice(BigDecimal unitListPrice) {
        this.setUnitListPrice(unitListPrice);
        return this;
    }

    public void setUnitListPrice(BigDecimal unitListPrice) {
        this.unitListPrice = unitListPrice;
    }

    public Boolean getIsModifiedPrice() {
        return this.isModifiedPrice;
    }

    public InvoiceItem isModifiedPrice(Boolean isModifiedPrice) {
        this.setIsModifiedPrice(isModifiedPrice);
        return this;
    }

    public void setIsModifiedPrice(Boolean isModifiedPrice) {
        this.isModifiedPrice = isModifiedPrice;
    }

    public Instant getEstimatedShipDate() {
        return this.estimatedShipDate;
    }

    public InvoiceItem estimatedShipDate(Instant estimatedShipDate) {
        this.setEstimatedShipDate(estimatedShipDate);
        return this;
    }

    public void setEstimatedShipDate(Instant estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public Instant getEstimatedDeliveryDate() {
        return this.estimatedDeliveryDate;
    }

    public InvoiceItem estimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return this;
    }

    public void setEstimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Instant getShipBeforeDate() {
        return this.shipBeforeDate;
    }

    public InvoiceItem shipBeforeDate(Instant shipBeforeDate) {
        this.setShipBeforeDate(shipBeforeDate);
        return this;
    }

    public void setShipBeforeDate(Instant shipBeforeDate) {
        this.shipBeforeDate = shipBeforeDate;
    }

    public Instant getShipAfterDate() {
        return this.shipAfterDate;
    }

    public InvoiceItem shipAfterDate(Instant shipAfterDate) {
        this.setShipAfterDate(shipAfterDate);
        return this;
    }

    public void setShipAfterDate(Instant shipAfterDate) {
        this.shipAfterDate = shipAfterDate;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public InvoiceItem entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public String getAttr1() {
        return this.attr1;
    }

    public InvoiceItem attr1(String attr1) {
        this.setAttr1(attr1);
        return this;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return this.attr2;
    }

    public InvoiceItem attr2(String attr2) {
        this.setAttr2(attr2);
        return this;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return this.attr3;
    }

    public InvoiceItem attr3(String attr3) {
        this.setAttr3(attr3);
        return this;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceItem invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public InvoiceItemType getInvoiceItemType() {
        return this.invoiceItemType;
    }

    public void setInvoiceItemType(InvoiceItemType invoiceItemType) {
        this.invoiceItemType = invoiceItemType;
    }

    public InvoiceItem invoiceItemType(InvoiceItemType invoiceItemType) {
        this.setInvoiceItemType(invoiceItemType);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InvoiceItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public InvoiceItem uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public ProductFeature getProductFeature() {
        return this.productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public InvoiceItem productFeature(ProductFeature productFeature) {
        this.setProductFeature(productFeature);
        return this;
    }

    public ProductFacility getProductFacility() {
        return this.productFacility;
    }

    public void setProductFacility(ProductFacility productFacility) {
        this.productFacility = productFacility;
    }

    public InvoiceItem productFacility(ProductFacility productFacility) {
        this.setProductFacility(productFacility);
        return this;
    }

    public InventoryItem getFromInventoryItem() {
        return this.fromInventoryItem;
    }

    public void setFromInventoryItem(InventoryItem inventoryItem) {
        this.fromInventoryItem = inventoryItem;
    }

    public InvoiceItem fromInventoryItem(InventoryItem inventoryItem) {
        this.setFromInventoryItem(inventoryItem);
        return this;
    }

    public TaxAuthorityRateType getTaxAuthRate() {
        return this.taxAuthRate;
    }

    public void setTaxAuthRate(TaxAuthorityRateType taxAuthorityRateType) {
        this.taxAuthRate = taxAuthorityRateType;
    }

    public InvoiceItem taxAuthRate(TaxAuthorityRateType taxAuthorityRateType) {
        this.setTaxAuthRate(taxAuthorityRateType);
        return this;
    }

    public Invoice getParentInvoice() {
        return this.parentInvoice;
    }

    public void setParentInvoice(Invoice invoice) {
        this.parentInvoice = invoice;
    }

    public InvoiceItem parentInvoice(Invoice invoice) {
        this.setParentInvoice(invoice);
        return this;
    }

    public InvoiceItem getParentInvoiceItem() {
        return this.parentInvoiceItem;
    }

    public void setParentInvoiceItem(InvoiceItem invoiceItem) {
        this.parentInvoiceItem = invoiceItem;
    }

    public InvoiceItem parentInvoiceItem(InvoiceItem invoiceItem) {
        this.setParentInvoiceItem(invoiceItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItem)) {
            return false;
        }
        return getId() != null && getId().equals(((InvoiceItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
            ", amount=" + getAmount() +
            ", cancelQuantity=" + getCancelQuantity() +
            ", selectedAmount=" + getSelectedAmount() +
            ", unitPrice=" + getUnitPrice() +
            ", unitListPrice=" + getUnitListPrice() +
            ", isModifiedPrice='" + getIsModifiedPrice() + "'" +
            ", estimatedShipDate='" + getEstimatedShipDate() + "'" +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", shipBeforeDate='" + getShipBeforeDate() + "'" +
            ", shipAfterDate='" + getShipAfterDate() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", attr1='" + getAttr1() + "'" +
            ", attr2='" + getAttr2() + "'" +
            ", attr3='" + getAttr3() + "'" +
            "}";
    }
}
