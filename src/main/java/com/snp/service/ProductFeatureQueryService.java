package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeature;
import com.snp.repository.ProductFeatureRepository;
import com.snp.service.criteria.ProductFeatureCriteria;
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
 * Service for executing complex queries for {@link ProductFeature} entities in the database.
 * The main input is a {@link ProductFeatureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFeature} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureQueryService extends QueryService<ProductFeature> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureQueryService.class);

    private final ProductFeatureRepository productFeatureRepository;

    public ProductFeatureQueryService(ProductFeatureRepository productFeatureRepository) {
        this.productFeatureRepository = productFeatureRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFeature} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFeature> findByCriteria(ProductFeatureCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFeature> specification = createSpecification(criteria);
        return productFeatureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeature> specification = createSpecification(criteria);
        return productFeatureRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeature> createSpecification(ProductFeatureCriteria criteria) {
        Specification<ProductFeature> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFeature_.productFeatureType, JoinType.LEFT);
                root.fetch(ProductFeature_.productFeatureCategory, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeature_.id),
                    buildStringSpecification(criteria.getName(), ProductFeature_.name),
                    buildStringSpecification(criteria.getDescription(), ProductFeature_.description),
                    buildRangeSpecification(criteria.getAmount(), ProductFeature_.amount),
                    buildRangeSpecification(criteria.getSequenceNo(), ProductFeature_.sequenceNo),
                    buildStringSpecification(criteria.getAbbrev(), ProductFeature_.abbrev),
                    buildSpecification(criteria.getProductFeatureTypeId(), root ->
                        root.join(ProductFeature_.productFeatureType, JoinType.LEFT).get(ProductFeatureType_.id)
                    ),
                    buildSpecification(criteria.getProductFeatureCategoryId(), root ->
                        root.join(ProductFeature_.productFeatureCategory, JoinType.LEFT).get(ProductFeatureCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
