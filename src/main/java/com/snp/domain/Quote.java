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
 * A Quote.
 */
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quote implements Serializable {

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
    @Column(name = "offer_no", length = 60)
    private String offerNo;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "valid_from_date")
    private Instant validFromDate;

    @Column(name = "valid_thru_date")
    private Instant validThruDate;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "offer_value", precision = 21, scale = 2)
    private BigDecimal offerValue;

    @Size(max = 2000)
    @Column(name = "remarks", length = 2000)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuoteType quoteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "custRequestType", "custRequestCategory", "status", "fromParty", "productStore", "salesChannel", "currencyUom" },
        allowSetters = true
    )
    private CustRequest custRequest;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "admin" }, allowSetters = true)
    private ProductStore productStore;

    @ManyToOne(fetch = FetchType.LAZY)
    private SalesChannel salesChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom currencyUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fromUom", "toUom" }, allowSetters = true)
    private UomConversion currencyConversion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Quote name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferNo() {
        return this.offerNo;
    }

    public Quote offerNo(String offerNo) {
        this.setOfferNo(offerNo);
        return this;
    }

    public void setOfferNo(String offerNo) {
        this.offerNo = offerNo;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public Quote issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Instant getValidFromDate() {
        return this.validFromDate;
    }

    public Quote validFromDate(Instant validFromDate) {
        this.setValidFromDate(validFromDate);
        return this;
    }

    public void setValidFromDate(Instant validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Instant getValidThruDate() {
        return this.validThruDate;
    }

    public Quote validThruDate(Instant validThruDate) {
        this.setValidThruDate(validThruDate);
        return this;
    }

    public void setValidThruDate(Instant validThruDate) {
        this.validThruDate = validThruDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Quote description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getOfferValue() {
        return this.offerValue;
    }

    public Quote offerValue(BigDecimal offerValue) {
        this.setOfferValue(offerValue);
        return this;
    }

    public void setOfferValue(BigDecimal offerValue) {
        this.offerValue = offerValue;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Quote remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public QuoteType getQuoteType() {
        return this.quoteType;
    }

    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
    }

    public Quote quoteType(QuoteType quoteType) {
        this.setQuoteType(quoteType);
        return this;
    }

    public CustRequest getCustRequest() {
        return this.custRequest;
    }

    public void setCustRequest(CustRequest custRequest) {
        this.custRequest = custRequest;
    }

    public Quote custRequest(CustRequest custRequest) {
        this.setCustRequest(custRequest);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Quote party(Party party) {
        this.setParty(party);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Quote status(Status status) {
        this.setStatus(status);
        return this;
    }

    public ProductStore getProductStore() {
        return this.productStore;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public Quote productStore(ProductStore productStore) {
        this.setProductStore(productStore);
        return this;
    }

    public SalesChannel getSalesChannel() {
        return this.salesChannel;
    }

    public void setSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    public Quote salesChannel(SalesChannel salesChannel) {
        this.setSalesChannel(salesChannel);
        return this;
    }

    public Uom getCurrencyUom() {
        return this.currencyUom;
    }

    public void setCurrencyUom(Uom uom) {
        this.currencyUom = uom;
    }

    public Quote currencyUom(Uom uom) {
        this.setCurrencyUom(uom);
        return this;
    }

    public UomConversion getCurrencyConversion() {
        return this.currencyConversion;
    }

    public void setCurrencyConversion(UomConversion uomConversion) {
        this.currencyConversion = uomConversion;
    }

    public Quote currencyConversion(UomConversion uomConversion) {
        this.setCurrencyConversion(uomConversion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quote)) {
            return false;
        }
        return getId() != null && getId().equals(((Quote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quote{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", offerNo='" + getOfferNo() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", validFromDate='" + getValidFromDate() + "'" +
            ", validThruDate='" + getValidThruDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", offerValue=" + getOfferValue() +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
