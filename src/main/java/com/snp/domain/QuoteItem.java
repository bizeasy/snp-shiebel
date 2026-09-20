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
 * A QuoteItem.
 */
@Entity
@Table(name = "quote_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteItem implements Serializable {

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

    @Column(name = "selected_amount", precision = 21, scale = 2)
    private BigDecimal selectedAmount;

    @Column(name = "quote_unit_price", precision = 21, scale = 2)
    private BigDecimal quoteUnitPrice;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "estimated_delivery_date")
    private Instant estimatedDeliveryDate;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Size(max = 2000)
    @Column(name = "comments", length = 2000)
    private String comments;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "quoteType", "custRequest", "party", "status", "productStore", "salesChannel", "currencyUom", "currencyConversion" },
        allowSetters = true
    )
    private Quote quote;

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
    @JsonIgnoreProperties(value = { "productFeatureType", "productFeatureCategory" }, allowSetters = true)
    private ProductFeature productFeature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uomType" }, allowSetters = true)
    private Uom uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "custRequestType", "custRequestCategory", "status", "fromParty", "productStore", "salesChannel", "currencyUom" },
        allowSetters = true
    )
    private CustRequest custRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "custRequest", "status", "product", "uom", "shoppingListItem" }, allowSetters = true)
    private CustRequestItem custRequestItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuoteItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public QuoteItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public QuoteItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSelectedAmount() {
        return this.selectedAmount;
    }

    public QuoteItem selectedAmount(BigDecimal selectedAmount) {
        this.setSelectedAmount(selectedAmount);
        return this;
    }

    public void setSelectedAmount(BigDecimal selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimal getQuoteUnitPrice() {
        return this.quoteUnitPrice;
    }

    public QuoteItem quoteUnitPrice(BigDecimal quoteUnitPrice) {
        this.setQuoteUnitPrice(quoteUnitPrice);
        return this;
    }

    public void setQuoteUnitPrice(BigDecimal quoteUnitPrice) {
        this.quoteUnitPrice = quoteUnitPrice;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public QuoteItem totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getEstimatedDeliveryDate() {
        return this.estimatedDeliveryDate;
    }

    public QuoteItem estimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return this;
    }

    public void setEstimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Integer getLeadTimeDays() {
        return this.leadTimeDays;
    }

    public QuoteItem leadTimeDays(Integer leadTimeDays) {
        this.setLeadTimeDays(leadTimeDays);
        return this;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public String getComments() {
        return this.comments;
    }

    public QuoteItem comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Quote getQuote() {
        return this.quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public QuoteItem quote(Quote quote) {
        this.setQuote(quote);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public QuoteItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductFeature getProductFeature() {
        return this.productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public QuoteItem productFeature(ProductFeature productFeature) {
        this.setProductFeature(productFeature);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public QuoteItem uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public QuoteItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public CustRequest getCustRequest() {
        return this.custRequest;
    }

    public void setCustRequest(CustRequest custRequest) {
        this.custRequest = custRequest;
    }

    public QuoteItem custRequest(CustRequest custRequest) {
        this.setCustRequest(custRequest);
        return this;
    }

    public CustRequestItem getCustRequestItem() {
        return this.custRequestItem;
    }

    public void setCustRequestItem(CustRequestItem custRequestItem) {
        this.custRequestItem = custRequestItem;
    }

    public QuoteItem custRequestItem(CustRequestItem custRequestItem) {
        this.setCustRequestItem(custRequestItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuoteItem)) {
            return false;
        }
        return getId() != null && getId().equals(((QuoteItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
            ", selectedAmount=" + getSelectedAmount() +
            ", quoteUnitPrice=" + getQuoteUnitPrice() +
            ", totalAmount=" + getTotalAmount() +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", leadTimeDays=" + getLeadTimeDays() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
