package com.snp.service.criteria;

import com.snp.domain.enumeration.RecurrenceFrequency;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.CashBook} entity. This class is used
 * in {@link com.snp.web.rest.CashBookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cash-books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CashBookCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RecurrenceFrequency
     */
    public static class RecurrenceFrequencyFilter extends Filter<RecurrenceFrequency> {

        public RecurrenceFrequencyFilter() {}

        public RecurrenceFrequencyFilter(RecurrenceFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public RecurrenceFrequencyFilter copy() {
            return new RecurrenceFrequencyFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter transactionDate;

    private LocalDateFilter dueDate;

    private BigDecimalFilter amount;

    private StringFilter description;

    private StringFilter vendorCustomerName;

    private StringFilter invoiceNo;

    private LocalDateFilter invoiceDate;

    private StringFilter purpose;

    private StringFilter remarks;

    private StringFilter project;

    private StringFilter usageUnits;

    private BooleanFilter recurringTransaction;

    private RecurrenceFrequencyFilter frequency;

    private LongFilter transactionTypeId;

    private LongFilter transactionCategoryId;

    private LongFilter paymentMethodTypeId;

    private LongFilter departmentId;

    private LongFilter currencyUomId;

    private LongFilter statusId;

    private LongFilter orderId;

    private LongFilter invoiceId;

    private LongFilter fromPartyId;

    private LongFilter toPartyId;

    private Boolean distinct;

    public CashBookCriteria() {}

    public CashBookCriteria(CashBookCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.transactionDate = other.optionalTransactionDate().map(LocalDateFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(LocalDateFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.vendorCustomerName = other.optionalVendorCustomerName().map(StringFilter::copy).orElse(null);
        this.invoiceNo = other.optionalInvoiceNo().map(StringFilter::copy).orElse(null);
        this.invoiceDate = other.optionalInvoiceDate().map(LocalDateFilter::copy).orElse(null);
        this.purpose = other.optionalPurpose().map(StringFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.project = other.optionalProject().map(StringFilter::copy).orElse(null);
        this.usageUnits = other.optionalUsageUnits().map(StringFilter::copy).orElse(null);
        this.recurringTransaction = other.optionalRecurringTransaction().map(BooleanFilter::copy).orElse(null);
        this.frequency = other.optionalFrequency().map(RecurrenceFrequencyFilter::copy).orElse(null);
        this.transactionTypeId = other.optionalTransactionTypeId().map(LongFilter::copy).orElse(null);
        this.transactionCategoryId = other.optionalTransactionCategoryId().map(LongFilter::copy).orElse(null);
        this.paymentMethodTypeId = other.optionalPaymentMethodTypeId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.toPartyId = other.optionalToPartyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CashBookCriteria copy() {
        return new CashBookCriteria(this);
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

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public Optional<LocalDateFilter> optionalTransactionDate() {
        return Optional.ofNullable(transactionDate);
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            setTransactionDate(new LocalDateFilter());
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public Optional<LocalDateFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public LocalDateFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new LocalDateFilter());
        }
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getVendorCustomerName() {
        return vendorCustomerName;
    }

    public Optional<StringFilter> optionalVendorCustomerName() {
        return Optional.ofNullable(vendorCustomerName);
    }

    public StringFilter vendorCustomerName() {
        if (vendorCustomerName == null) {
            setVendorCustomerName(new StringFilter());
        }
        return vendorCustomerName;
    }

    public void setVendorCustomerName(StringFilter vendorCustomerName) {
        this.vendorCustomerName = vendorCustomerName;
    }

    public StringFilter getInvoiceNo() {
        return invoiceNo;
    }

    public Optional<StringFilter> optionalInvoiceNo() {
        return Optional.ofNullable(invoiceNo);
    }

    public StringFilter invoiceNo() {
        if (invoiceNo == null) {
            setInvoiceNo(new StringFilter());
        }
        return invoiceNo;
    }

    public void setInvoiceNo(StringFilter invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDateFilter getInvoiceDate() {
        return invoiceDate;
    }

    public Optional<LocalDateFilter> optionalInvoiceDate() {
        return Optional.ofNullable(invoiceDate);
    }

    public LocalDateFilter invoiceDate() {
        if (invoiceDate == null) {
            setInvoiceDate(new LocalDateFilter());
        }
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public StringFilter getPurpose() {
        return purpose;
    }

    public Optional<StringFilter> optionalPurpose() {
        return Optional.ofNullable(purpose);
    }

    public StringFilter purpose() {
        if (purpose == null) {
            setPurpose(new StringFilter());
        }
        return purpose;
    }

    public void setPurpose(StringFilter purpose) {
        this.purpose = purpose;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public Optional<StringFilter> optionalRemarks() {
        return Optional.ofNullable(remarks);
    }

    public StringFilter remarks() {
        if (remarks == null) {
            setRemarks(new StringFilter());
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public StringFilter getProject() {
        return project;
    }

    public Optional<StringFilter> optionalProject() {
        return Optional.ofNullable(project);
    }

    public StringFilter project() {
        if (project == null) {
            setProject(new StringFilter());
        }
        return project;
    }

    public void setProject(StringFilter project) {
        this.project = project;
    }

    public StringFilter getUsageUnits() {
        return usageUnits;
    }

    public Optional<StringFilter> optionalUsageUnits() {
        return Optional.ofNullable(usageUnits);
    }

    public StringFilter usageUnits() {
        if (usageUnits == null) {
            setUsageUnits(new StringFilter());
        }
        return usageUnits;
    }

    public void setUsageUnits(StringFilter usageUnits) {
        this.usageUnits = usageUnits;
    }

    public BooleanFilter getRecurringTransaction() {
        return recurringTransaction;
    }

    public Optional<BooleanFilter> optionalRecurringTransaction() {
        return Optional.ofNullable(recurringTransaction);
    }

    public BooleanFilter recurringTransaction() {
        if (recurringTransaction == null) {
            setRecurringTransaction(new BooleanFilter());
        }
        return recurringTransaction;
    }

    public void setRecurringTransaction(BooleanFilter recurringTransaction) {
        this.recurringTransaction = recurringTransaction;
    }

    public RecurrenceFrequencyFilter getFrequency() {
        return frequency;
    }

    public Optional<RecurrenceFrequencyFilter> optionalFrequency() {
        return Optional.ofNullable(frequency);
    }

    public RecurrenceFrequencyFilter frequency() {
        if (frequency == null) {
            setFrequency(new RecurrenceFrequencyFilter());
        }
        return frequency;
    }

    public void setFrequency(RecurrenceFrequencyFilter frequency) {
        this.frequency = frequency;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public Optional<LongFilter> optionalTransactionTypeId() {
        return Optional.ofNullable(transactionTypeId);
    }

    public LongFilter transactionTypeId() {
        if (transactionTypeId == null) {
            setTransactionTypeId(new LongFilter());
        }
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public LongFilter getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public Optional<LongFilter> optionalTransactionCategoryId() {
        return Optional.ofNullable(transactionCategoryId);
    }

    public LongFilter transactionCategoryId() {
        if (transactionCategoryId == null) {
            setTransactionCategoryId(new LongFilter());
        }
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(LongFilter transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
    }

    public LongFilter getPaymentMethodTypeId() {
        return paymentMethodTypeId;
    }

    public Optional<LongFilter> optionalPaymentMethodTypeId() {
        return Optional.ofNullable(paymentMethodTypeId);
    }

    public LongFilter paymentMethodTypeId() {
        if (paymentMethodTypeId == null) {
            setPaymentMethodTypeId(new LongFilter());
        }
        return paymentMethodTypeId;
    }

    public void setPaymentMethodTypeId(LongFilter paymentMethodTypeId) {
        this.paymentMethodTypeId = paymentMethodTypeId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public Optional<LongFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new LongFilter());
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
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
        final CashBookCriteria that = (CashBookCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(vendorCustomerName, that.vendorCustomerName) &&
            Objects.equals(invoiceNo, that.invoiceNo) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(purpose, that.purpose) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(project, that.project) &&
            Objects.equals(usageUnits, that.usageUnits) &&
            Objects.equals(recurringTransaction, that.recurringTransaction) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(transactionCategoryId, that.transactionCategoryId) &&
            Objects.equals(paymentMethodTypeId, that.paymentMethodTypeId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(toPartyId, that.toPartyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transactionDate,
            dueDate,
            amount,
            description,
            vendorCustomerName,
            invoiceNo,
            invoiceDate,
            purpose,
            remarks,
            project,
            usageUnits,
            recurringTransaction,
            frequency,
            transactionTypeId,
            transactionCategoryId,
            paymentMethodTypeId,
            departmentId,
            currencyUomId,
            statusId,
            orderId,
            invoiceId,
            fromPartyId,
            toPartyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CashBookCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTransactionDate().map(f -> "transactionDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalVendorCustomerName().map(f -> "vendorCustomerName=" + f + ", ").orElse("") +
            optionalInvoiceNo().map(f -> "invoiceNo=" + f + ", ").orElse("") +
            optionalInvoiceDate().map(f -> "invoiceDate=" + f + ", ").orElse("") +
            optionalPurpose().map(f -> "purpose=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalProject().map(f -> "project=" + f + ", ").orElse("") +
            optionalUsageUnits().map(f -> "usageUnits=" + f + ", ").orElse("") +
            optionalRecurringTransaction().map(f -> "recurringTransaction=" + f + ", ").orElse("") +
            optionalFrequency().map(f -> "frequency=" + f + ", ").orElse("") +
            optionalTransactionTypeId().map(f -> "transactionTypeId=" + f + ", ").orElse("") +
            optionalTransactionCategoryId().map(f -> "transactionCategoryId=" + f + ", ").orElse("") +
            optionalPaymentMethodTypeId().map(f -> "paymentMethodTypeId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalInvoiceId().map(f -> "invoiceId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalToPartyId().map(f -> "toPartyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
