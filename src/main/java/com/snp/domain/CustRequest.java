package com.snp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustRequest.
 */
@Entity
@Table(name = "cust_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "priority")
    private Integer priority;

    @NotNull
    @Column(name = "cust_request_date", nullable = false)
    private Instant custRequestDate;

    @Column(name = "response_required_date")
    private Instant responseRequiredDate;

    @Column(name = "open_date_time")
    private Instant openDateTime;

    @Column(name = "closed_date_time")
    private Instant closedDateTime;

    @Size(max = 2000)
    @Column(name = "internal_comment", length = 2000)
    private String internalComment;

    @Column(name = "billed")
    private Boolean billed;

    @Column(name = "cust_estimated_duration_days")
    private Integer custEstimatedDurationDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "party" }, allowSetters = true)
    private CustRequestType custRequestType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "custRequestType" }, allowSetters = true)
    private CustRequestCategory custRequestCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party fromParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "admin" }, allowSetters = true)
    private ProductStore productStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private SalesChannel salesChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CustRequest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CustRequest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public CustRequest priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Instant getCustRequestDate() {
        return this.custRequestDate;
    }

    public CustRequest custRequestDate(Instant custRequestDate) {
        this.setCustRequestDate(custRequestDate);
        return this;
    }

    public void setCustRequestDate(Instant custRequestDate) {
        this.custRequestDate = custRequestDate;
    }

    public Instant getResponseRequiredDate() {
        return this.responseRequiredDate;
    }

    public CustRequest responseRequiredDate(Instant responseRequiredDate) {
        this.setResponseRequiredDate(responseRequiredDate);
        return this;
    }

    public void setResponseRequiredDate(Instant responseRequiredDate) {
        this.responseRequiredDate = responseRequiredDate;
    }

    public Instant getOpenDateTime() {
        return this.openDateTime;
    }

    public CustRequest openDateTime(Instant openDateTime) {
        this.setOpenDateTime(openDateTime);
        return this;
    }

    public void setOpenDateTime(Instant openDateTime) {
        this.openDateTime = openDateTime;
    }

    public Instant getClosedDateTime() {
        return this.closedDateTime;
    }

    public CustRequest closedDateTime(Instant closedDateTime) {
        this.setClosedDateTime(closedDateTime);
        return this;
    }

    public void setClosedDateTime(Instant closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public String getInternalComment() {
        return this.internalComment;
    }

    public CustRequest internalComment(String internalComment) {
        this.setInternalComment(internalComment);
        return this;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public Boolean getBilled() {
        return this.billed;
    }

    public CustRequest billed(Boolean billed) {
        this.setBilled(billed);
        return this;
    }

    public void setBilled(Boolean billed) {
        this.billed = billed;
    }

    public Integer getCustEstimatedDurationDays() {
        return this.custEstimatedDurationDays;
    }

    public CustRequest custEstimatedDurationDays(Integer custEstimatedDurationDays) {
        this.setCustEstimatedDurationDays(custEstimatedDurationDays);
        return this;
    }

    public void setCustEstimatedDurationDays(Integer custEstimatedDurationDays) {
        this.custEstimatedDurationDays = custEstimatedDurationDays;
    }

    public CustRequestType getCustRequestType() {
        return this.custRequestType;
    }

    public void setCustRequestType(CustRequestType custRequestType) {
        this.custRequestType = custRequestType;
    }

    public CustRequest custRequestType(CustRequestType custRequestType) {
        this.setCustRequestType(custRequestType);
        return this;
    }

    public CustRequestCategory getCustRequestCategory() {
        return this.custRequestCategory;
    }

    public void setCustRequestCategory(CustRequestCategory custRequestCategory) {
        this.custRequestCategory = custRequestCategory;
    }

    public CustRequest custRequestCategory(CustRequestCategory custRequestCategory) {
        this.setCustRequestCategory(custRequestCategory);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CustRequest status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Party getFromParty() {
        return this.fromParty;
    }

    public void setFromParty(Party party) {
        this.fromParty = party;
    }

    public CustRequest fromParty(Party party) {
        this.setFromParty(party);
        return this;
    }

    public ProductStore getProductStore() {
        return this.productStore;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public CustRequest productStore(ProductStore productStore) {
        this.setProductStore(productStore);
        return this;
    }

    public SalesChannel getSalesChannel() {
        return this.salesChannel;
    }

    public void setSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    public CustRequest salesChannel(SalesChannel salesChannel) {
        this.setSalesChannel(salesChannel);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public CustRequest currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((CustRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority=" + getPriority() +
            ", custRequestDate='" + getCustRequestDate() + "'" +
            ", responseRequiredDate='" + getResponseRequiredDate() + "'" +
            ", openDateTime='" + getOpenDateTime() + "'" +
            ", closedDateTime='" + getClosedDateTime() + "'" +
            ", internalComment='" + getInternalComment() + "'" +
            ", billed='" + getBilled() + "'" +
            ", custEstimatedDurationDays=" + getCustEstimatedDurationDays() +
            "}";
    }
}
