package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snp.domain.enumeration.RecurrenceFrequency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CashBook.
 */
@Entity
@Table(name = "cash_book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CashBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Size(max = 200)
    @Column(name = "vendor_customer_name", length = 200)
    private String vendorCustomerName;

    @Size(max = 60)
    @Column(name = "invoice_no", length = 60)
    private String invoiceNo;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Size(max = 255)
    @Column(name = "purpose", length = 255)
    private String purpose;

    @Size(max = 2000)
    @Column(name = "remarks", length = 2000)
    private String remarks;

    @Size(max = 255)
    @Column(name = "project", length = 255)
    private String project;

    @Size(max = 60)
    @Column(name = "usage_units", length = 60)
    private String usageUnits;

    @Column(name = "recurring_transaction")
    private Boolean recurringTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private RecurrenceFrequency frequency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    private TransactionCategory transactionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethodType paymentMethodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "head", "productStore" }, allowSetters = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "invoiceType", "order", "fromParty", "toParty", "roleType", "status", "contactMech", "currencyUom" },
        allowSetters = true
    )
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party fromParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party toParty;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CashBook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public CashBook transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public CashBook dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CashBook amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public CashBook description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendorCustomerName() {
        return this.vendorCustomerName;
    }

    public CashBook vendorCustomerName(String vendorCustomerName) {
        this.setVendorCustomerName(vendorCustomerName);
        return this;
    }

    public void setVendorCustomerName(String vendorCustomerName) {
        this.vendorCustomerName = vendorCustomerName;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public CashBook invoiceNo(String invoiceNo) {
        this.setInvoiceNo(invoiceNo);
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public CashBook invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public CashBook purpose(String purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public CashBook remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProject() {
        return this.project;
    }

    public CashBook project(String project) {
        this.setProject(project);
        return this;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUsageUnits() {
        return this.usageUnits;
    }

    public CashBook usageUnits(String usageUnits) {
        this.setUsageUnits(usageUnits);
        return this;
    }

    public void setUsageUnits(String usageUnits) {
        this.usageUnits = usageUnits;
    }

    public Boolean getRecurringTransaction() {
        return this.recurringTransaction;
    }

    public CashBook recurringTransaction(Boolean recurringTransaction) {
        this.setRecurringTransaction(recurringTransaction);
        return this;
    }

    public void setRecurringTransaction(Boolean recurringTransaction) {
        this.recurringTransaction = recurringTransaction;
    }

    public RecurrenceFrequency getFrequency() {
        return this.frequency;
    }

    public CashBook frequency(RecurrenceFrequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(RecurrenceFrequency frequency) {
        this.frequency = frequency;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public CashBook transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public TransactionCategory getTransactionCategory() {
        return this.transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public CashBook transactionCategory(TransactionCategory transactionCategory) {
        this.setTransactionCategory(transactionCategory);
        return this;
    }

    public PaymentMethodType getPaymentMethodType() {
        return this.paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public CashBook paymentMethodType(PaymentMethodType paymentMethodType) {
        this.setPaymentMethodType(paymentMethodType);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public CashBook department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public CashBook currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CashBook status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CashBook order(Order order) {
        this.setOrder(order);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public CashBook invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public CashBook fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public Party getToParty() {
        return this.toParty;
    }

    public void setToParty(Party party) {
        this.toParty = party;
    }

    public CashBook toParty(Party party) {
        this.setToParty(party);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashBook)) {
            return false;
        }
        return getId() != null && getId().equals(((CashBook) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CashBook{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", vendorCustomerName='" + getVendorCustomerName() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", purpose='" + getPurpose() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", project='" + getProject() + "'" +
            ", usageUnits='" + getUsageUnits() + "'" +
            ", recurringTransaction='" + getRecurringTransaction() + "'" +
            ", frequency='" + getFrequency() + "'" +
            "}";
    }
}
