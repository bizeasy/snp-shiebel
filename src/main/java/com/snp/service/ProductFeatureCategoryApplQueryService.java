package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureCategoryAppl;
import com.snp.repository.ProductFeatureCategoryApplRepository;
import com.snp.service.criteria.ProductFeatureCategoryApplCriteria;
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
 * Service for executing complex queries for {@link ProductFeatureCategoryAppl} entities in the database.
 * The main input is a {@link ProductFeatureCategoryApplCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFeatureCategoryAppl} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureCategoryApplQueryService extends QueryService<ProductFeatureCategoryAppl> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureCategoryApplQueryService.class);

    private final ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository;

    public ProductFeatureCategoryApplQueryService(ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository) {
        this.productFeatureCategoryApplRepository = productFeatureCategoryApplRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFeatureCategoryAppl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFeatureCategoryAppl> findByCriteria(ProductFeatureCategoryApplCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFeatureCategoryAppl> specification = createSpecification(criteria);
        return productFeatureCategoryApplRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureCategoryApplCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureCategoryAppl> specification = createSpecification(criteria);
        return productFeatureCategoryApplRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureCategoryApplCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureCategoryAppl> createSpecification(ProductFeatureCategoryApplCriteria criteria) {
        Specification<ProductFeatureCategoryAppl> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFeatureCategoryAppl_.productCategory, JoinType.LEFT);
                root.fetch(ProductFeatureCategoryAppl_.productFeatureCategory, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureCategoryAppl_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductFeatureCategoryAppl_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductFeatureCategoryAppl_.thruDate),
                    buildSpecification(criteria.getProductCategoryId(), root ->
                        root.join(ProductFeatureCategoryAppl_.productCategory, JoinType.LEFT).get(ProductCategory_.id)
                    ),
                    buildSpecification(criteria.getProductFeatureCategoryId(), root ->
                        root.join(ProductFeatureCategoryAppl_.productFeatureCategory, JoinType.LEFT).get(ProductFeatureCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
