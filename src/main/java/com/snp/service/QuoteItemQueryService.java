package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.QuoteItem;
import com.snp.repository.QuoteItemRepository;
import com.snp.service.criteria.QuoteItemCriteria;
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
 * Service for executing complex queries for {@link QuoteItem} entities in the database.
 * The main input is a {@link QuoteItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuoteItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteItemQueryService extends QueryService<QuoteItem> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteItemQueryService.class);

    private final QuoteItemRepository quoteItemRepository;

    public QuoteItemQueryService(QuoteItemRepository quoteItemRepository) {
        this.quoteItemRepository = quoteItemRepository;
    }

    /**
     * Return a {@link Page} of {@link QuoteItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteItem> findByCriteria(QuoteItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuoteItem> specification = createSpecification(criteria);
        return quoteItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuoteItem> specification = createSpecification(criteria);
        return quoteItemRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuoteItem> createSpecification(QuoteItemCriteria criteria) {
        Specification<QuoteItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(QuoteItem_.quote, JoinType.LEFT);
                root.fetch(QuoteItem_.product, JoinType.LEFT);
                root.fetch(QuoteItem_.productFeature, JoinType.LEFT);
                root.fetch(QuoteItem_.uom, JoinType.LEFT);
                root.fetch(QuoteItem_.status, JoinType.LEFT);
                root.fetch(QuoteItem_.custRequest, JoinType.LEFT);
                root.fetch(QuoteItem_.custRequestItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), QuoteItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), QuoteItem_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), QuoteItem_.quantity),
                    buildRangeSpecification(criteria.getSelectedAmount(), QuoteItem_.selectedAmount),
                    buildRangeSpecification(criteria.getQuoteUnitPrice(), QuoteItem_.quoteUnitPrice),
                    buildRangeSpecification(criteria.getTotalAmount(), QuoteItem_.totalAmount),
                    buildRangeSpecification(criteria.getEstimatedDeliveryDate(), QuoteItem_.estimatedDeliveryDate),
                    buildRangeSpecification(criteria.getLeadTimeDays(), QuoteItem_.leadTimeDays),
                    buildStringSpecification(criteria.getComments(), QuoteItem_.comments),
                    buildSpecification(criteria.getQuoteId(), root -> root.join(QuoteItem_.quote, JoinType.LEFT).get(Quote_.id)),
                    buildSpecification(criteria.getProductId(), root -> root.join(QuoteItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(QuoteItem_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getUomId(), root -> root.join(QuoteItem_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(QuoteItem_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getCustRequestId(), root ->
                        root.join(QuoteItem_.custRequest, JoinType.LEFT).get(CustRequest_.id)
                    ),
                    buildSpecification(criteria.getCustRequestItemId(), root ->
                        root.join(QuoteItem_.custRequestItem, JoinType.LEFT).get(CustRequestItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
