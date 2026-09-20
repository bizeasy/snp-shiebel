package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureCategory;
import com.snp.repository.ProductFeatureCategoryRepository;
import com.snp.service.criteria.ProductFeatureCategoryCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductFeatureCategory} entities in the database.
 * The main input is a {@link ProductFeatureCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductFeatureCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureCategoryQueryService extends QueryService<ProductFeatureCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureCategoryQueryService.class);

    private final ProductFeatureCategoryRepository productFeatureCategoryRepository;

    public ProductFeatureCategoryQueryService(ProductFeatureCategoryRepository productFeatureCategoryRepository) {
        this.productFeatureCategoryRepository = productFeatureCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link ProductFeatureCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductFeatureCategory> findByCriteria(ProductFeatureCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductFeatureCategory> specification = createSpecification(criteria);
        return productFeatureCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureCategory> specification = createSpecification(criteria);
        return productFeatureCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureCategory> createSpecification(ProductFeatureCategoryCriteria criteria) {
        Specification<ProductFeatureCategory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureCategory_.id),
                    buildStringSpecification(criteria.getName(), ProductFeatureCategory_.name),
                    buildStringSpecification(criteria.getDescription(), ProductFeatureCategory_.description)
                )
            );
        }
        return specification;
    }
}
