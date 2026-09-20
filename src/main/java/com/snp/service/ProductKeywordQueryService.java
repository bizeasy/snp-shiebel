package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductKeyword;
import com.snp.repository.ProductKeywordRepository;
import com.snp.service.criteria.ProductKeywordCriteria;
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
 * Service for executing complex queries for {@link ProductKeyword} entities in the database.
 * The main input is a {@link ProductKeywordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductKeyword} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductKeywordQueryService extends QueryService<ProductKeyword> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductKeywordQueryService.class);

    private final ProductKeywordRepository productKeywordRepository;

    public ProductKeywordQueryService(ProductKeywordRepository productKeywordRepository) {
        this.productKeywordRepository = productKeywordRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductKeyword} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductKeyword> findByCriteria(ProductKeywordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductKeyword> specification = createSpecification(criteria);
        return productKeywordRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductKeywordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductKeyword> specification = createSpecification(criteria);
        return productKeywordRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductKeywordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductKeyword> createSpecification(ProductKeywordCriteria criteria) {
        Specification<ProductKeyword> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductKeyword_.product, JoinType.LEFT);
                root.fetch(ProductKeyword_.keywordType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductKeyword_.id),
                    buildStringSpecification(criteria.getKeyword(), ProductKeyword_.keyword),
                    buildRangeSpecification(criteria.getRelevancyWeight(), ProductKeyword_.relevancyWeight),
                    buildSpecification(criteria.getProductId(), root -> root.join(ProductKeyword_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getKeywordTypeId(), root ->
                        root.join(ProductKeyword_.keywordType, JoinType.LEFT).get(KeywordType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
