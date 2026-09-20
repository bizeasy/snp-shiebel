package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.QuoteAdjustment;
import com.snp.repository.QuoteAdjustmentRepository;
import com.snp.service.criteria.QuoteAdjustmentCriteria;
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
 * Service for executing complex queries for {@link QuoteAdjustment} entities in the database.
 * The main input is a {@link QuoteAdjustmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuoteAdjustment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteAdjustmentQueryService extends QueryService<QuoteAdjustment> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteAdjustmentQueryService.class);

    private final QuoteAdjustmentRepository quoteAdjustmentRepository;

    public QuoteAdjustmentQueryService(QuoteAdjustmentRepository quoteAdjustmentRepository) {
        this.quoteAdjustmentRepository = quoteAdjustmentRepository;
    }

    /**
     * Return a {@link Page} of {@link QuoteAdjustment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteAdjustment> findByCriteria(QuoteAdjustmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuoteAdjustment> specification = createSpecification(criteria);
        return quoteAdjustmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteAdjustmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuoteAdjustment> specification = createSpecification(criteria);
        return quoteAdjustmentRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteAdjustmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuoteAdjustment> createSpecification(QuoteAdjustmentCriteria criteria) {
        Specification<QuoteAdjustment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(QuoteAdjustment_.quote, JoinType.LEFT);
                root.fetch(QuoteAdjustment_.quoteItem, JoinType.LEFT);
                root.fetch(QuoteAdjustment_.adjustmentType, JoinType.LEFT);
                root.fetch(QuoteAdjustment_.product, JoinType.LEFT);
                root.fetch(QuoteAdjustment_.productFeature, JoinType.LEFT);
                root.fetch(QuoteAdjustment_.taxAuthority, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), QuoteAdjustment_.id),
                    buildStringSpecification(criteria.getDescription(), QuoteAdjustment_.description),
                    buildStringSpecification(criteria.getComments(), QuoteAdjustment_.comments),
                    buildRangeSpecification(criteria.getAmount(), QuoteAdjustment_.amount),
                    buildRangeSpecification(criteria.getSourcePercentage(), QuoteAdjustment_.sourcePercentage),
                    buildStringSpecification(criteria.getSourceRefId(), QuoteAdjustment_.sourceRefId),
                    buildRangeSpecification(criteria.getExemptAmount(), QuoteAdjustment_.exemptAmount),
                    buildSpecification(criteria.getIncludeInTax(), QuoteAdjustment_.includeInTax),
                    buildSpecification(criteria.getIncludeInShipping(), QuoteAdjustment_.includeInShipping),
                    buildSpecification(criteria.getIncludedInPrice(), QuoteAdjustment_.includedInPrice),
                    buildRangeSpecification(criteria.getEntryDate(), QuoteAdjustment_.entryDate),
                    buildSpecification(criteria.getQuoteId(), root -> root.join(QuoteAdjustment_.quote, JoinType.LEFT).get(Quote_.id)),
                    buildSpecification(criteria.getQuoteItemId(), root ->
                        root.join(QuoteAdjustment_.quoteItem, JoinType.LEFT).get(QuoteItem_.id)
                    ),
                    buildSpecification(criteria.getAdjustmentTypeId(), root ->
                        root.join(QuoteAdjustment_.adjustmentType, JoinType.LEFT).get(OrderAdjustmentType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(QuoteAdjustment_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(QuoteAdjustment_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getTaxAuthorityId(), root ->
                        root.join(QuoteAdjustment_.taxAuthority, JoinType.LEFT).get(TaxAuthority_.id)
                    )
                )
            );
        }
        return specification;
    }
}
