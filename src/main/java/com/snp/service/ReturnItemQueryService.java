package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ReturnItem;
import com.snp.repository.ReturnItemRepository;
import com.snp.service.criteria.ReturnItemCriteria;
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
 * Service for executing complex queries for {@link ReturnItem} entities in the database.
 * The main input is a {@link ReturnItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReturnItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnItemQueryService extends QueryService<ReturnItem> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnItemQueryService.class);

    private final ReturnItemRepository returnItemRepository;

    public ReturnItemQueryService(ReturnItemRepository returnItemRepository) {
        this.returnItemRepository = returnItemRepository;
    }

    /**
     * Return a {@link Page} of {@link ReturnItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReturnItem> findByCriteria(ReturnItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReturnItem> specification = createSpecification(criteria);
        return returnItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReturnItem> specification = createSpecification(criteria);
        return returnItemRepository.count(specification);
    }

    /**
     * Function to convert {@link ReturnItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReturnItem> createSpecification(ReturnItemCriteria criteria) {
        Specification<ReturnItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ReturnItem_.returnHeader, JoinType.LEFT);
                root.fetch(ReturnItem_.returnType, JoinType.LEFT);
                root.fetch(ReturnItem_.product, JoinType.LEFT);
                root.fetch(ReturnItem_.orderItem, JoinType.LEFT);
                root.fetch(ReturnItem_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ReturnItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), ReturnItem_.sequenceNo),
                    buildStringSpecification(criteria.getDescription(), ReturnItem_.description),
                    buildRangeSpecification(criteria.getReturnQuantity(), ReturnItem_.returnQuantity),
                    buildRangeSpecification(criteria.getReceivedQuantity(), ReturnItem_.receivedQuantity),
                    buildRangeSpecification(criteria.getReturnPrice(), ReturnItem_.returnPrice),
                    buildStringSpecification(criteria.getReturnItemPurpose(), ReturnItem_.returnItemPurpose),
                    buildSpecification(criteria.getReturnHeaderId(), root ->
                        root.join(ReturnItem_.returnHeader, JoinType.LEFT).get(ReturnHeader_.id)
                    ),
                    buildSpecification(criteria.getReturnTypeId(), root ->
                        root.join(ReturnItem_.returnType, JoinType.LEFT).get(ReturnHeaderType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(ReturnItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(ReturnItem_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(ReturnItem_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
