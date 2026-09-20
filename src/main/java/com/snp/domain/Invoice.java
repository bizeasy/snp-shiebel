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
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private Instant invoiceDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "paid_date")
    private Instant paidDate;

    @Size(max = 60)
    @Column(name = "reference_number", length = 60)
    private String referenceNumber;

    @Size(max = 255)
    @Column(name = "invoice_message", length = 255)
    private String invoiceMessage;

    @Size(max = 25)
    @Column(name = "external_id", length = 25)
    private String externalId;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "grand_total", precision = 21, scale = 2)
    private BigDecimal grandTotal;

    @Size(max = 255)
    @Column(name = "project_name", length = 255)
    private String projectName;

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

    @ManyToOne(optional = false)
    @NotNull
    private InvoiceType invoiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "orderType",
            "salesChannel",
            "party",
            "supplier",
            "status",
            "currencyUom",
            "currencyConversion",
            "quote",
            "facility",
            "productStore",
            "department",
            "customTimePeriod",
        },
        allowSetters = true
    )
    private Order order;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party fromParty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party toParty;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contactMechType" }, allowSetters = true)
    private ContactMech contactMech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(Instant invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public Invoice dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getPaidDate() {
        return this.paidDate;
    }

    public Invoice paidDate(Instant paidDate) {
        this.setPaidDate(paidDate);
        return this;
    }

    public void setPaidDate(Instant paidDate) {
        this.paidDate = paidDate;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public Invoice referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getInvoiceMessage() {
        return this.invoiceMessage;
    }

    public Invoice invoiceMessage(String invoiceMessage) {
        this.setInvoiceMessage(invoiceMessage);
        return this;
    }

    public void setInvoiceMessage(String invoiceMessage) {
        this.invoiceMessage = invoiceMessage;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Invoice externalId(String externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public Invoice entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getGrandTotal() {
        return this.grandTotal;
    }

    public Invoice grandTotal(BigDecimal grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Invoice projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return this.description;
    }

    public Invoice description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Invoice remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentTerms() {
        return this.paymentTerms;
    }

    public Invoice paymentTerms(String paymentTerms) {
        this.setPaymentTerms(paymentTerms);
        return this;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getDeliveryTerms() {
        return this.deliveryTerms;
    }

    public Invoice deliveryTerms(String deliveryTerms) {
        this.setDeliveryTerms(deliveryTerms);
        return this;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getInfo() {
        return this.info;
    }

    public Invoice info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public InvoiceType getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Invoice invoiceType(InvoiceType invoiceType) {
        this.setInvoiceType(invoiceType);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Invoice order(Order order) {
        this.setOrder(order);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public Invoice fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public Party getToParty() {
        return this.toParty;
    }

    public void setToParty(Party party) {
        this.toParty = party;
    }

    public Invoice toParty(Party party) {
        this.setToParty(party);
        return this;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Invoice roleType(RoleType roleType) {
        this.setRoleType(roleType);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Invoice status(Status status) {
        this.setStatus(status);
        return this;
    }

    public ContactMech getContactMech() {
        return this.contactMech;
    }

    public void setContactMech(ContactMech contactMech) {
        this.contactMech = contactMech;
    }

    public Invoice contactMech(ContactMech contactMech) {
        this.setContactMech(contactMech);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public Invoice currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", paidDate='" + getPaidDate() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", invoiceMessage='" + getInvoiceMessage() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", grandTotal=" + getGrandTotal() +
            ", projectName='" + getProjectName() + "'" +
            ", description='" + getDescription() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", paymentTerms='" + getPaymentTerms() + "'" +
            ", deliveryTerms='" + getDeliveryTerms() + "'" +
            ", info='" + getInfo() + "'" +
            "}";
    }
}
