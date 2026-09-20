package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderItem;
import com.snp.repository.OrderItemRepository;
import com.snp.service.criteria.OrderItemCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrderItem} entities in the database.
 * The main input is a {@link OrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemQueryService extends QueryService<OrderItem> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemQueryService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemQueryService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItem> findByCriteria(OrderItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItem> createSpecification(OrderItemCriteria criteria) {
        Specification<OrderItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderItem_.order, JoinType.LEFT);
                root.fetch(OrderItem_.orderItemType, JoinType.LEFT);
                root.fetch(OrderItem_.product, JoinType.LEFT);
                root.fetch(OrderItem_.uom, JoinType.LEFT);
                root.fetch(OrderItem_.supplierProduct, JoinType.LEFT);
                root.fetch(OrderItem_.supplier, JoinType.LEFT);
                root.fetch(OrderItem_.productFeature, JoinType.LEFT);
                root.fetch(OrderItem_.productFacility, JoinType.LEFT);
                root.fetch(OrderItem_.fromInventoryItem, JoinType.LEFT);
                root.fetch(OrderItem_.shoppingListItem, JoinType.LEFT);
                root.fetch(OrderItem_.quoteItem, JoinType.LEFT);
                root.fetch(OrderItem_.status, JoinType.LEFT);
                root.fetch(OrderItem_.taxAuthRate, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), OrderItem_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), OrderItem_.quantity),
                    buildRangeSpecification(criteria.getCancelQuantity(), OrderItem_.cancelQuantity),
                    buildRangeSpecification(criteria.getSelectedAmount(), OrderItem_.selectedAmount),
                    buildRangeSpecification(criteria.getUnitPrice(), OrderItem_.unitPrice),
                    buildRangeSpecification(criteria.getUnitListPrice(), OrderItem_.unitListPrice),
                    buildSpecification(criteria.getIsModifiedPrice(), OrderItem_.isModifiedPrice),
                    buildRangeSpecification(criteria.getEstimatedShipDate(), OrderItem_.estimatedShipDate),
                    buildRangeSpecification(criteria.getEstimatedDeliveryDate(), OrderItem_.estimatedDeliveryDate),
                    buildRangeSpecification(criteria.getShipBeforeDate(), OrderItem_.shipBeforeDate),
                    buildRangeSpecification(criteria.getShipAfterDate(), OrderItem_.shipAfterDate),
                    buildRangeSpecification(criteria.getEntryDate(), OrderItem_.entryDate),
                    buildStringSpecification(criteria.getAttr1(), OrderItem_.attr1),
                    buildStringSpecification(criteria.getAttr2(), OrderItem_.attr2),
                    buildStringSpecification(criteria.getAttr3(), OrderItem_.attr3),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderItem_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemTypeId(), root ->
                        root.join(OrderItem_.orderItemType, JoinType.LEFT).get(OrderItemType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(OrderItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getUomId(), root -> root.join(OrderItem_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getSupplierProductId(), root ->
                        root.join(OrderItem_.supplierProduct, JoinType.LEFT).get(SupplierProduct_.id)
                    ),
                    buildSpecification(criteria.getSupplierId(), root -> root.join(OrderItem_.supplier, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(OrderItem_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getProductFacilityId(), root ->
                        root.join(OrderItem_.productFacility, JoinType.LEFT).get(ProductFacility_.id)
                    ),
                    buildSpecification(criteria.getFromInventoryItemId(), root ->
                        root.join(OrderItem_.fromInventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getShoppingListItemId(), root ->
                        root.join(OrderItem_.shoppingListItem, JoinType.LEFT).get(ShoppingListItem_.id)
                    ),
                    buildSpecification(criteria.getQuoteItemId(), root ->
                        root.join(OrderItem_.quoteItem, JoinType.LEFT).get(QuoteItem_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(OrderItem_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getTaxAuthRateId(), root ->
                        root.join(OrderItem_.taxAuthRate, JoinType.LEFT).get(TaxAuthorityRateType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
