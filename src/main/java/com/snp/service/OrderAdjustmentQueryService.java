package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.OrderAdjustment;
import com.snp.repository.OrderAdjustmentRepository;
import com.snp.service.criteria.OrderAdjustmentCriteria;
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
 * Service for executing complex queries for {@link OrderAdjustment} entities in the database.
 * The main input is a {@link OrderAdjustmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderAdjustment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderAdjustmentQueryService extends QueryService<OrderAdjustment> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAdjustmentQueryService.class);

    private final OrderAdjustmentRepository orderAdjustmentRepository;

    public OrderAdjustmentQueryService(OrderAdjustmentRepository orderAdjustmentRepository) {
        this.orderAdjustmentRepository = orderAdjustmentRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderAdjustment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderAdjustment> findByCriteria(OrderAdjustmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderAdjustment> specification = createSpecification(criteria);
        return orderAdjustmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderAdjustmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderAdjustment> specification = createSpecification(criteria);
        return orderAdjustmentRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderAdjustmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderAdjustment> createSpecification(OrderAdjustmentCriteria criteria) {
        Specification<OrderAdjustment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(OrderAdjustment_.order, JoinType.LEFT);
                root.fetch(OrderAdjustment_.orderItem, JoinType.LEFT);
                root.fetch(OrderAdjustment_.adjustmentType, JoinType.LEFT);
                root.fetch(OrderAdjustment_.product, JoinType.LEFT);
                root.fetch(OrderAdjustment_.productFeature, JoinType.LEFT);
                root.fetch(OrderAdjustment_.taxAuthority, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrderAdjustment_.id),
                    buildStringSpecification(criteria.getDescription(), OrderAdjustment_.description),
                    buildStringSpecification(criteria.getComments(), OrderAdjustment_.comments),
                    buildRangeSpecification(criteria.getAmount(), OrderAdjustment_.amount),
                    buildRangeSpecification(criteria.getSourcePercentage(), OrderAdjustment_.sourcePercentage),
                    buildStringSpecification(criteria.getSourceRefId(), OrderAdjustment_.sourceRefId),
                    buildRangeSpecification(criteria.getExemptAmount(), OrderAdjustment_.exemptAmount),
                    buildSpecification(criteria.getIncludeInTax(), OrderAdjustment_.includeInTax),
                    buildSpecification(criteria.getIncludeInShipping(), OrderAdjustment_.includeInShipping),
                    buildSpecification(criteria.getIncludedInPrice(), OrderAdjustment_.includedInPrice),
                    buildRangeSpecification(criteria.getEntryDate(), OrderAdjustment_.entryDate),
                    buildSpecification(criteria.getOrderId(), root -> root.join(OrderAdjustment_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(OrderAdjustment_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getAdjustmentTypeId(), root ->
                        root.join(OrderAdjustment_.adjustmentType, JoinType.LEFT).get(OrderAdjustmentType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(OrderAdjustment_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(OrderAdjustment_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getTaxAuthorityId(), root ->
                        root.join(OrderAdjustment_.taxAuthority, JoinType.LEFT).get(TaxAuthority_.id)
                    )
                )
            );
        }
        return specification;
    }
}
