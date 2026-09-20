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
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem implements Serializable {

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
    private OrderItemType orderItemType;

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
    @JsonIgnoreProperties(value = { "product", "party", "quantityUom", "currencyUom" }, allowSetters = true)
    private SupplierProduct supplierProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "partyType", "status", "address" }, allowSetters = true)
    private Party supplier;

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
    @JsonIgnoreProperties(value = { "shoppingList", "product", "status" }, allowSetters = true)
    private ShoppingListItem shoppingListItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "quote", "product", "productFeature", "uom", "status", "custRequest", "custRequestItem" },
        allowSetters = true
    )
    private QuoteItem quoteItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "taxAuthority", "taxSlab" }, allowSetters = true)
    private TaxAuthorityRateType taxAuthRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    public OrderItem sequenceNo(Integer sequenceNo) {
        this.setSequenceNo(sequenceNo);
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCancelQuantity() {
        return this.cancelQuantity;
    }

    public OrderItem cancelQuantity(BigDecimal cancelQuantity) {
        this.setCancelQuantity(cancelQuantity);
        return this;
    }

    public void setCancelQuantity(BigDecimal cancelQuantity) {
        this.cancelQuantity = cancelQuantity;
    }

    public BigDecimal getSelectedAmount() {
        return this.selectedAmount;
    }

    public OrderItem selectedAmount(BigDecimal selectedAmount) {
        this.setSelectedAmount(selectedAmount);
        return this;
    }

    public void setSelectedAmount(BigDecimal selectedAmount) {
        this.selectedAmount = selectedAmount;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public OrderItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitListPrice() {
        return this.unitListPrice;
    }

    public OrderItem unitListPrice(BigDecimal unitListPrice) {
        this.setUnitListPrice(unitListPrice);
        return this;
    }

    public void setUnitListPrice(BigDecimal unitListPrice) {
        this.unitListPrice = unitListPrice;
    }

    public Boolean getIsModifiedPrice() {
        return this.isModifiedPrice;
    }

    public OrderItem isModifiedPrice(Boolean isModifiedPrice) {
        this.setIsModifiedPrice(isModifiedPrice);
        return this;
    }

    public void setIsModifiedPrice(Boolean isModifiedPrice) {
        this.isModifiedPrice = isModifiedPrice;
    }

    public Instant getEstimatedShipDate() {
        return this.estimatedShipDate;
    }

    public OrderItem estimatedShipDate(Instant estimatedShipDate) {
        this.setEstimatedShipDate(estimatedShipDate);
        return this;
    }

    public void setEstimatedShipDate(Instant estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public Instant getEstimatedDeliveryDate() {
        return this.estimatedDeliveryDate;
    }

    public OrderItem estimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return this;
    }

    public void setEstimatedDeliveryDate(Instant estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Instant getShipBeforeDate() {
        return this.shipBeforeDate;
    }

    public OrderItem shipBeforeDate(Instant shipBeforeDate) {
        this.setShipBeforeDate(shipBeforeDate);
        return this;
    }

    public void setShipBeforeDate(Instant shipBeforeDate) {
        this.shipBeforeDate = shipBeforeDate;
    }

    public Instant getShipAfterDate() {
        return this.shipAfterDate;
    }

    public OrderItem shipAfterDate(Instant shipAfterDate) {
        this.setShipAfterDate(shipAfterDate);
        return this;
    }

    public void setShipAfterDate(Instant shipAfterDate) {
        this.shipAfterDate = shipAfterDate;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public OrderItem entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public String getAttr1() {
        return this.attr1;
    }

    public OrderItem attr1(String attr1) {
        this.setAttr1(attr1);
        return this;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return this.attr2;
    }

    public OrderItem attr2(String attr2) {
        this.setAttr2(attr2);
        return this;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return this.attr3;
    }

    public OrderItem attr3(String attr3) {
        this.setAttr3(attr3);
        return this;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderItemType getOrderItemType() {
        return this.orderItemType;
    }

    public void setOrderItemType(OrderItemType orderItemType) {
        this.orderItemType = orderItemType;
    }

    public OrderItem orderItemType(OrderItemType orderItemType) {
        this.setOrderItemType(orderItemType);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Uom getUom() {
        return this.uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public OrderItem uom(Uom uom) {
        this.setUom(uom);
        return this;
    }

    public SupplierProduct getSupplierProduct() {
        return this.supplierProduct;
    }

    public void setSupplierProduct(SupplierProduct supplierProduct) {
        this.supplierProduct = supplierProduct;
    }

    public OrderItem supplierProduct(SupplierProduct supplierProduct) {
        this.setSupplierProduct(supplierProduct);
        return this;
    }

    public Party getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Party party) {
        this.supplier = party;
    }

    public OrderItem supplier(Party party) {
        this.setSupplier(party);
        return this;
    }

    public ProductFeature getProductFeature() {
        return this.productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public OrderItem productFeature(ProductFeature productFeature) {
        this.setProductFeature(productFeature);
        return this;
    }

    public ProductFacility getProductFacility() {
        return this.productFacility;
    }

    public void setProductFacility(ProductFacility productFacility) {
        this.productFacility = productFacility;
    }

    public OrderItem productFacility(ProductFacility productFacility) {
        this.setProductFacility(productFacility);
        return this;
    }

    public InventoryItem getFromInventoryItem() {
        return this.fromInventoryItem;
    }

    public void setFromInventoryItem(InventoryItem inventoryItem) {
        this.fromInventoryItem = inventoryItem;
    }

    public OrderItem fromInventoryItem(InventoryItem inventoryItem) {
        this.setFromInventoryItem(inventoryItem);
        return this;
    }

    public ShoppingListItem getShoppingListItem() {
        return this.shoppingListItem;
    }

    public void setShoppingListItem(ShoppingListItem shoppingListItem) {
        this.shoppingListItem = shoppingListItem;
    }

    public OrderItem shoppingListItem(ShoppingListItem shoppingListItem) {
        this.setShoppingListItem(shoppingListItem);
        return this;
    }

    public QuoteItem getQuoteItem() {
        return this.quoteItem;
    }

    public void setQuoteItem(QuoteItem quoteItem) {
        this.quoteItem = quoteItem;
    }

    public OrderItem quoteItem(QuoteItem quoteItem) {
        this.setQuoteItem(quoteItem);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OrderItem status(Status status) {
        this.setStatus(status);
        return this;
    }

    public TaxAuthorityRateType getTaxAuthRate() {
        return this.taxAuthRate;
    }

    public void setTaxAuthRate(TaxAuthorityRateType taxAuthorityRateType) {
        this.taxAuthRate = taxAuthorityRateType;
    }

    public OrderItem taxAuthRate(TaxAuthorityRateType taxAuthorityRateType) {
        this.setTaxAuthRate(taxAuthorityRateType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", sequenceNo=" + getSequenceNo() +
            ", quantity=" + getQuantity() +
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
