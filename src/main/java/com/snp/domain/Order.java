package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "sys_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 60)
    @Column(name = "order_ref_no", length = 60)
    private String orderRefNo;

    @Size(max = 25)
    @Column(name = "external_id", length = 25)
    private String externalId;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "priority")
    private Integer priority;

    @Column(name = "is_rush_order")
    private Boolean isRushOrder;

    @Column(name = "needs_inventory_issuance")
    private Boolean needsInventoryIssuance;

    @Column(name = "remaining_sub_total", precision = 21, scale = 2)
    private BigDecimal remainingSubTotal;

    @Column(name = "grand_total", precision = 21, scale = 2)
    private BigDecimal grandTotal;

    @Size(max = 255)
    @Column(name = "project_name", length = 255)
    private String projectName;

    @Column(name = "version")
    private Integer version;

    @Size(max = 60)
    @Column(name = "version_ref", length = 60)
    private String versionRef;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 2000)
    @Column(name = "remarks", length = 2000)
    private String remarks;

    @Lob
    @Column(name = "payment_terms")
    private String paymentTerms;

    @Lob
    @Column(name = "delivery_terms")
    private String deliveryTerms;

    @Lob
    @Column(name = "info")
    private String info;

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
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    private SalesChannel salesChannel;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fromUom", "toUom" }, allowSetters = true)
    private UomConversion currencyConversion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "quoteType", "custRequest", "party", "status", "productStore", "salesChannel", "currencyUom", "currencyConversion" },
        allowSetters = true
    )
    private Quote quote;

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
    @JsonIgnoreProperties(value = { "owner", "admin" }, allowSetters = true)
    private ProductStore productStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "head", "productStore" }, allowSetters = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomTimePeriod customTimePeriod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Order name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderRefNo() {
        return this.orderRefNo;
    }

    public Order orderRefNo(String orderRefNo) {
        this.setOrderRefNo(orderRefNo);
        return this;
    }

    public void setOrderRefNo(String orderRefNo) {
        this.orderRefNo = orderRefNo;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Order externalId(String externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public Order entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getDeliveryDate() {
        return this.deliveryDate;
    }

    public Order deliveryDate(LocalDate deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public Order priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsRushOrder() {
        return this.isRushOrder;
    }

    public Order isRushOrder(Boolean isRushOrder) {
        this.setIsRushOrder(isRushOrder);
        return this;
    }

    public void setIsRushOrder(Boolean isRushOrder) {
        this.isRushOrder = isRushOrder;
    }

    public Boolean getNeedsInventoryIssuance() {
        return this.needsInventoryIssuance;
    }

    public Order needsInventoryIssuance(Boolean needsInventoryIssuance) {
        this.setNeedsInventoryIssuance(needsInventoryIssuance);
        return this;
    }

    public void setNeedsInventoryIssuance(Boolean needsInventoryIssuance) {
        this.needsInventoryIssuance = needsInventoryIssuance;
    }

    public BigDecimal getRemainingSubTotal() {
        return this.remainingSubTotal;
    }

    public Order remainingSubTotal(BigDecimal remainingSubTotal) {
        this.setRemainingSubTotal(remainingSubTotal);
        return this;
    }

    public void setRemainingSubTotal(BigDecimal remainingSubTotal) {
        this.remainingSubTotal = remainingSubTotal;
    }

    public BigDecimal getGrandTotal() {
        return this.grandTotal;
    }

    public Order grandTotal(BigDecimal grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Order projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Order version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVersionRef() {
        return this.versionRef;
    }

    public Order versionRef(String versionRef) {
        this.setVersionRef(versionRef);
        return this;
    }

    public void setVersionRef(String versionRef) {
        this.versionRef = versionRef;
    }

    public String getDescription() {
        return this.description;
    }

    public Order description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Order remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentTerms() {
        return this.paymentTerms;
    }

    public Order paymentTerms(String paymentTerms) {
        this.setPaymentTerms(paymentTerms);
        return this;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getDeliveryTerms() {
        return this.deliveryTerms;
    }

    public Order deliveryTerms(String deliveryTerms) {
        this.setDeliveryTerms(deliveryTerms);
        return this;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getInfo() {
        return this.info;
    }

    public Order info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAttr1() {
        return this.attr1;
    }

    public Order attr1(String attr1) {
        this.setAttr1(attr1);
        return this;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return this.attr2;
    }

    public Order attr2(String attr2) {
        this.setAttr2(attr2);
        return this;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return this.attr3;
    }

    public Order attr3(String attr3) {
        this.setAttr3(attr3);
        return this;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Order orderType(OrderType orderType) {
        this.setOrderType(orderType);
        return this;
    }

    public SalesChannel getSalesChannel() {
        return this.salesChannel;
    }

    public void setSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    public Order salesChannel(SalesChannel salesChannel) {
        this.setSalesChannel(salesChannel);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Order party(Party party) {
        this.setParty(party);
        return this;
    }

    public Party getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Party party) {
        this.supplier = party;
    }

    public Order supplier(Party party) {
        this.setSupplier(party);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Order status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public Order currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    public UomConversion getCurrencyConversion() {
        return this.currencyConversion;
    }

    public void setCurrencyConversion(UomConversion uomConversion) {
        this.currencyConversion = uomConversion;
    }

    public Order currencyConversion(UomConversion uomConversion) {
        this.setCurrencyConversion(uomConversion);
        return this;
    }

    public Quote getQuote() {
        return this.quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Order quote(Quote quote) {
        this.setQuote(quote);
        return this;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Order facility(Facility facility) {
        this.setFacility(facility);
        return this;
    }

    public ProductStore getProductStore() {
        return this.productStore;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public Order productStore(ProductStore productStore) {
        this.setProductStore(productStore);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Order department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public CustomTimePeriod getCustomTimePeriod() {
        return this.customTimePeriod;
    }

    public void setCustomTimePeriod(CustomTimePeriod customTimePeriod) {
        this.customTimePeriod = customTimePeriod;
    }

    public Order customTimePeriod(CustomTimePeriod customTimePeriod) {
        this.setCustomTimePeriod(customTimePeriod);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", orderRefNo='" + getOrderRefNo() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", priority=" + getPriority() +
            ", isRushOrder='" + getIsRushOrder() + "'" +
            ", needsInventoryIssuance='" + getNeedsInventoryIssuance() + "'" +
            ", remainingSubTotal=" + getRemainingSubTotal() +
            ", grandTotal=" + getGrandTotal() +
            ", projectName='" + getProjectName() + "'" +
            ", version=" + getVersion() +
            ", versionRef='" + getVersionRef() + "'" +
            ", description='" + getDescription() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", paymentTerms='" + getPaymentTerms() + "'" +
            ", deliveryTerms='" + getDeliveryTerms() + "'" +
            ", info='" + getInfo() + "'" +
            ", attr1='" + getAttr1() + "'" +
            ", attr2='" + getAttr2() + "'" +
            ", attr3='" + getAttr3() + "'" +
            "}";
    }
}
