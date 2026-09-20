package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestItem;
import com.snp.repository.CustRequestItemRepository;
import com.snp.service.criteria.CustRequestItemCriteria;
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
 * Service for executing complex queries for {@link CustRequestItem} entities in the database.
 * The main input is a {@link CustRequestItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustRequestItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestItemQueryService extends QueryService<CustRequestItem> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestItemQueryService.class);

    private final CustRequestItemRepository custRequestItemRepository;

    public CustRequestItemQueryService(CustRequestItemRepository custRequestItemRepository) {
        this.custRequestItemRepository = custRequestItemRepository;
    }

    /**
     * Return a {@link Page} of {@link CustRequestItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustRequestItem> findByCriteria(CustRequestItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustRequestItem> specification = createSpecification(criteria);
        return custRequestItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestItem> specification = createSpecification(criteria);
        return custRequestItemRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestItem> createSpecification(CustRequestItemCriteria criteria) {
        Specification<CustRequestItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestItem_.custRequest, JoinType.LEFT);
                root.fetch(CustRequestItem_.status, JoinType.LEFT);
                root.fetch(CustRequestItem_.product, JoinType.LEFT);
                root.fetch(CustRequestItem_.uom, JoinType.LEFT);
                root.fetch(CustRequestItem_.shoppingListItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), CustRequestItem_.sequenceNo),
                    buildRangeSpecification(criteria.getPriority(), CustRequestItem_.priority),
                    buildRangeSpecification(criteria.getRequiredByDate(), CustRequestItem_.requiredByDate),
                    buildRangeSpecification(criteria.getQuantity(), CustRequestItem_.quantity),
                    buildRangeSpecification(criteria.getSelectedAmount(), CustRequestItem_.selectedAmount),
                    buildRangeSpecification(criteria.getMaxAmount(), CustRequestItem_.maxAmount),
                    buildStringSpecification(criteria.getDescription(), CustRequestItem_.description),
                    buildStringSpecification(criteria.getStory(), CustRequestItem_.story),
                    buildSpecification(criteria.getCustRequestId(), root ->
                        root.join(CustRequestItem_.custRequest, JoinType.LEFT).get(CustRequest_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(CustRequestItem_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(CustRequestItem_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getUomId(), root -> root.join(CustRequestItem_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getShoppingListItemId(), root ->
                        root.join(CustRequestItem_.shoppingListItem, JoinType.LEFT).get(ShoppingListItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
