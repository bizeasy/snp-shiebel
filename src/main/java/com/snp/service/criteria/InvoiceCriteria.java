package com.snp.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.snp.domain.Invoice} entity. This class is used
 * in {@link com.snp.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter invoiceDate;

    private InstantFilter dueDate;

    private InstantFilter paidDate;

    private StringFilter referenceNumber;

    private StringFilter invoiceMessage;

    private StringFilter externalId;

    private InstantFilter entryDate;

    private BigDecimalFilter grandTotal;

    private StringFilter projectName;

    private StringFilter remarks;

    private LongFilter invoiceTypeId;

    private LongFilter orderId;

    private LongFilter fromPartyId;

    private LongFilter toPartyId;

    private LongFilter roleTypeId;

    private LongFilter statusId;

    private LongFilter contactMechId;

    private LongFilter currencyUomId;

    private Boolean distinct;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.invoiceDate = other.optionalInvoiceDate().map(InstantFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.paidDate = other.optionalPaidDate().map(InstantFilter::copy).orElse(null);
        this.referenceNumber = other.optionalReferenceNumber().map(StringFilter::copy).orElse(null);
        this.invoiceMessage = other.optionalInvoiceMessage().map(StringFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.entryDate = other.optionalEntryDate().map(InstantFilter::copy).orElse(null);
        this.grandTotal = other.optionalGrandTotal().map(BigDecimalFilter::copy).orElse(null);
        this.projectName = other.optionalProjectName().map(StringFilter::copy).orElse(null);
        this.remarks = other.optionalRemarks().map(StringFilter::copy).orElse(null);
        this.invoiceTypeId = other.optionalInvoiceTypeId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.fromPartyId = other.optionalFromPartyId().map(LongFilter::copy).orElse(null);
        this.toPartyId = other.optionalToPartyId().map(LongFilter::copy).orElse(null);
        this.roleTypeId = other.optionalRoleTypeId().map(LongFilter::copy).orElse(null);
        this.statusId = other.optionalStatusId().map(LongFilter::copy).orElse(null);
        this.contactMechId = other.optionalContactMechId().map(LongFilter::copy).orElse(null);
        this.currencyUomId = other.optionalCurrencyUomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
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

    public InstantFilter getInvoiceDate() {
        return invoiceDate;
    }

    public Optional<InstantFilter> optionalInvoiceDate() {
        return Optional.ofNullable(invoiceDate);
    }

    public InstantFilter invoiceDate() {
        if (invoiceDate == null) {
            setInvoiceDate(new InstantFilter());
        }
        return invoiceDate;
    }

    public void setInvoiceDate(InstantFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public Optional<InstantFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new InstantFilter());
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public InstantFilter getPaidDate() {
        return paidDate;
    }

    public Optional<InstantFilter> optionalPaidDate() {
        return Optional.ofNullable(paidDate);
    }

    public InstantFilter paidDate() {
        if (paidDate == null) {
            setPaidDate(new InstantFilter());
        }
        return paidDate;
    }

    public void setPaidDate(InstantFilter paidDate) {
        this.paidDate = paidDate;
    }

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public Optional<StringFilter> optionalReferenceNumber() {
        return Optional.ofNullable(referenceNumber);
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            setReferenceNumber(new StringFilter());
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public StringFilter getInvoiceMessage() {
        return invoiceMessage;
    }

    public Optional<StringFilter> optionalInvoiceMessage() {
        return Optional.ofNullable(invoiceMessage);
    }

    public StringFilter invoiceMessage() {
        if (invoiceMessage == null) {
            setInvoiceMessage(new StringFilter());
        }
        return invoiceMessage;
    }

    public void setInvoiceMessage(StringFilter invoiceMessage) {
        this.invoiceMessage = invoiceMessage;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public Optional<StringFilter> optionalExternalId() {
        return Optional.ofNullable(externalId);
    }

    public StringFilter externalId() {
        if (externalId == null) {
            setExternalId(new StringFilter());
        }
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public InstantFilter getEntryDate() {
        return entryDate;
    }

    public Optional<InstantFilter> optionalEntryDate() {
        return Optional.ofNullable(entryDate);
    }

    public InstantFilter entryDate() {
        if (entryDate == null) {
            setEntryDate(new InstantFilter());
        }
        return entryDate;
    }

    public void setEntryDate(InstantFilter entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimalFilter getGrandTotal() {
        return grandTotal;
    }

    public Optional<BigDecimalFilter> optionalGrandTotal() {
        return Optional.ofNullable(grandTotal);
    }

    public BigDecimalFilter grandTotal() {
        if (grandTotal == null) {
            setGrandTotal(new BigDecimalFilter());
        }
        return grandTotal;
    }

    public void setGrandTotal(BigDecimalFilter grandTotal) {
        this.grandTotal = grandTotal;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public Optional<StringFilter> optionalProjectName() {
        return Optional.ofNullable(projectName);
    }

    public StringFilter projectName() {
        if (projectName == null) {
            setProjectName(new StringFilter());
        }
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
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

    public LongFilter getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public Optional<LongFilter> optionalInvoiceTypeId() {
        return Optional.ofNullable(invoiceTypeId);
    }

    public LongFilter invoiceTypeId() {
        if (invoiceTypeId == null) {
            setInvoiceTypeId(new LongFilter());
        }
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(LongFilter invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(paidDate, that.paidDate) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(invoiceMessage, that.invoiceMessage) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(grandTotal, that.grandTotal) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(invoiceTypeId, that.invoiceTypeId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(fromPartyId, that.fromPartyId) &&
            Objects.equals(toPartyId, that.toPartyId) &&
            Objects.equals(roleTypeId, that.roleTypeId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(contactMechId, that.contactMechId) &&
            Objects.equals(currencyUomId, that.currencyUomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            invoiceDate,
            dueDate,
            paidDate,
            referenceNumber,
            invoiceMessage,
            externalId,
            entryDate,
            grandTotal,
            projectName,
            remarks,
            invoiceTypeId,
            orderId,
            fromPartyId,
            toPartyId,
            roleTypeId,
            statusId,
            contactMechId,
            currencyUomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInvoiceDate().map(f -> "invoiceDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalPaidDate().map(f -> "paidDate=" + f + ", ").orElse("") +
            optionalReferenceNumber().map(f -> "referenceNumber=" + f + ", ").orElse("") +
            optionalInvoiceMessage().map(f -> "invoiceMessage=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalEntryDate().map(f -> "entryDate=" + f + ", ").orElse("") +
            optionalGrandTotal().map(f -> "grandTotal=" + f + ", ").orElse("") +
            optionalProjectName().map(f -> "projectName=" + f + ", ").orElse("") +
            optionalRemarks().map(f -> "remarks=" + f + ", ").orElse("") +
            optionalInvoiceTypeId().map(f -> "invoiceTypeId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalFromPartyId().map(f -> "fromPartyId=" + f + ", ").orElse("") +
            optionalToPartyId().map(f -> "toPartyId=" + f + ", ").orElse("") +
            optionalRoleTypeId().map(f -> "roleTypeId=" + f + ", ").orElse("") +
            optionalStatusId().map(f -> "statusId=" + f + ", ").orElse("") +
            optionalContactMechId().map(f -> "contactMechId=" + f + ", ").orElse("") +
            optionalCurrencyUomId().map(f -> "currencyUomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
