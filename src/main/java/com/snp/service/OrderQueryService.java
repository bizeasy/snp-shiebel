package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Order;
import com.snp.repository.OrderRepository;
import com.snp.service.criteria.OrderCriteria;
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
 * Service for executing complex queries for {@link Order} entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Order} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Return a {@link Page} of {@link Order} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Order> findByCriteria(OrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Order_.orderType, JoinType.LEFT);
                root.fetch(Order_.salesChannel, JoinType.LEFT);
                root.fetch(Order_.party, JoinType.LEFT);
                root.fetch(Order_.supplier, JoinType.LEFT);
                root.fetch(Order_.status, JoinType.LEFT);
                root.fetch(Order_.currencyUom, JoinType.LEFT);
                root.fetch(Order_.currencyConversion, JoinType.LEFT);
                root.fetch(Order_.quote, JoinType.LEFT);
                root.fetch(Order_.facility, JoinType.LEFT);
                root.fetch(Order_.productStore, JoinType.LEFT);
                root.fetch(Order_.department, JoinType.LEFT);
                root.fetch(Order_.customTimePeriod, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Order_.id),
                    buildStringSpecification(criteria.getName(), Order_.name),
                    buildStringSpecification(criteria.getOrderRefNo(), Order_.orderRefNo),
                    buildStringSpecification(criteria.getExternalId(), Order_.externalId),
                    buildRangeSpecification(criteria.getOrderDate(), Order_.orderDate),
                    buildRangeSpecification(criteria.getEntryDate(), Order_.entryDate),
                    buildRangeSpecification(criteria.getDeliveryDate(), Order_.deliveryDate),
                    buildRangeSpecification(criteria.getPriority(), Order_.priority),
                    buildSpecification(criteria.getIsRushOrder(), Order_.isRushOrder),
                    buildSpecification(criteria.getNeedsInventoryIssuance(), Order_.needsInventoryIssuance),
                    buildRangeSpecification(criteria.getRemainingSubTotal(), Order_.remainingSubTotal),
                    buildRangeSpecification(criteria.getGrandTotal(), Order_.grandTotal),
                    buildStringSpecification(criteria.getProjectName(), Order_.projectName),
                    buildRangeSpecification(criteria.getVersion(), Order_.version),
                    buildStringSpecification(criteria.getVersionRef(), Order_.versionRef),
                    buildStringSpecification(criteria.getRemarks(), Order_.remarks),
                    buildStringSpecification(criteria.getAttr1(), Order_.attr1),
                    buildStringSpecification(criteria.getAttr2(), Order_.attr2),
                    buildStringSpecification(criteria.getAttr3(), Order_.attr3),
                    buildSpecification(criteria.getOrderTypeId(), root -> root.join(Order_.orderType, JoinType.LEFT).get(OrderType_.id)),
                    buildSpecification(criteria.getSalesChannelId(), root ->
                        root.join(Order_.salesChannel, JoinType.LEFT).get(SalesChannel_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(Order_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getSupplierId(), root -> root.join(Order_.supplier, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Order_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(Order_.currencyUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getCurrencyConversionId(), root ->
                        root.join(Order_.currencyConversion, JoinType.LEFT).get(UomConversion_.id)
                    ),
                    buildSpecification(criteria.getQuoteId(), root -> root.join(Order_.quote, JoinType.LEFT).get(Quote_.id)),
                    buildSpecification(criteria.getFacilityId(), root -> root.join(Order_.facility, JoinType.LEFT).get(Facility_.id)),
                    buildSpecification(criteria.getProductStoreId(), root ->
                        root.join(Order_.productStore, JoinType.LEFT).get(ProductStore_.id)
                    ),
                    buildSpecification(criteria.getDepartmentId(), root -> root.join(Order_.department, JoinType.LEFT).get(Department_.id)),
                    buildSpecification(criteria.getCustomTimePeriodId(), root ->
                        root.join(Order_.customTimePeriod, JoinType.LEFT).get(CustomTimePeriod_.id)
                    )
                )
            );
        }
        return specification;
    }
}
