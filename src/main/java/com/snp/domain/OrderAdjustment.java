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
 * A OrderAdjustment.
 */
@Entity
@Table(name = "order_adjustment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderAdjustment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 2000)
    @Column(name = "comments", length = 2000)
    private String comments;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "source_percentage", precision = 21, scale = 2)
    private BigDecimal sourcePercentage;

    @Size(max = 60)
    @Column(name = "source_ref_id", length = 60)
    private String sourceRefId;

    @Column(name = "exempt_amount", precision = 21, scale = 2)
    private BigDecimal exemptAmount;

    @Column(name = "include_in_tax")
    private Boolean includeInTax;

    @Column(name = "include_in_shipping")
    private Boolean includeInShipping;

    @Column(name = "included_in_price")
    private Boolean includedInPrice;

    @Column(name = "entry_date")
    private Instant entryDate;

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
        value = {
            "order",
            "orderItemType",
            "product",
            "uom",
            "supplierProduct",
            "supplier",
            "productFeature",
            "productFacility",
            "fromInventoryItem",
            "shoppingListItem",
            "quoteItem",
            "status",
            "taxAuthRate",
        },
        allowSetters = true
    )
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private OrderAdjustmentType adjustmentType;

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
    @JsonIgnoreProperties(value = { "geo" }, allowSetters = true)
    private TaxAuthority taxAuthority;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderAdjustment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public OrderAdjustment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return this.comments;
    }

    public OrderAdjustment comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public OrderAdjustment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSourcePercentage() {
        return this.sourcePercentage;
    }

    public OrderAdjustment sourcePercentage(BigDecimal sourcePercentage) {
        this.setSourcePercentage(sourcePercentage);
        return this;
    }

    public void setSourcePercentage(BigDecimal sourcePercentage) {
        this.sourcePercentage = sourcePercentage;
    }

    public String getSourceRefId() {
        return this.sourceRefId;
    }

    public OrderAdjustment sourceRefId(String sourceRefId) {
        this.setSourceRefId(sourceRefId);
        return this;
    }

    public void setSourceRefId(String sourceRefId) {
        this.sourceRefId = sourceRefId;
    }

    public BigDecimal getExemptAmount() {
        return this.exemptAmount;
    }

    public OrderAdjustment exemptAmount(BigDecimal exemptAmount) {
        this.setExemptAmount(exemptAmount);
        return this;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public Boolean getIncludeInTax() {
        return this.includeInTax;
    }

    public OrderAdjustment includeInTax(Boolean includeInTax) {
        this.setIncludeInTax(includeInTax);
        return this;
    }

    public void setIncludeInTax(Boolean includeInTax) {
        this.includeInTax = includeInTax;
    }

    public Boolean getIncludeInShipping() {
        return this.includeInShipping;
    }

    public OrderAdjustment includeInShipping(Boolean includeInShipping) {
        this.setIncludeInShipping(includeInShipping);
        return this;
    }

    public void setIncludeInShipping(Boolean includeInShipping) {
        this.includeInShipping = includeInShipping;
    }

    public Boolean getIncludedInPrice() {
        return this.includedInPrice;
    }

    public OrderAdjustment includedInPrice(Boolean includedInPrice) {
        this.setIncludedInPrice(includedInPrice);
        return this;
    }

    public void setIncludedInPrice(Boolean includedInPrice) {
        this.includedInPrice = includedInPrice;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public OrderAdjustment entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderAdjustment order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderAdjustment orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public OrderAdjustmentType getAdjustmentType() {
        return this.adjustmentType;
    }

    public void setAdjustmentType(OrderAdjustmentType orderAdjustmentType) {
        this.adjustmentType = orderAdjustmentType;
    }

    public OrderAdjustment adjustmentType(OrderAdjustmentType orderAdjustmentType) {
        this.setAdjustmentType(orderAdjustmentType);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderAdjustment product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductFeature getProductFeature() {
        return this.productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public OrderAdjustment productFeature(ProductFeature productFeature) {
        this.setProductFeature(productFeature);
        return this;
    }

    public TaxAuthority getTaxAuthority() {
        return this.taxAuthority;
    }

    public void setTaxAuthority(TaxAuthority taxAuthority) {
        this.taxAuthority = taxAuthority;
    }

    public OrderAdjustment taxAuthority(TaxAuthority taxAuthority) {
        this.setTaxAuthority(taxAuthority);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderAdjustment)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderAdjustment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderAdjustment{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", comments='" + getComments() + "'" +
            ", amount=" + getAmount() +
            ", sourcePercentage=" + getSourcePercentage() +
            ", sourceRefId='" + getSourceRefId() + "'" +
            ", exemptAmount=" + getExemptAmount() +
            ", includeInTax='" + getIncludeInTax() + "'" +
            ", includeInShipping='" + getIncludeInShipping() + "'" +
            ", includedInPrice='" + getIncludedInPrice() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            "}";
    }
}
