package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureApplType;
import com.snp.repository.ProductFeatureApplTypeRepository;
import com.snp.service.criteria.ProductFeatureApplTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductFeatureApplType} entities in the database.
 * The main input is a {@link ProductFeatureApplTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductFeatureApplType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureApplTypeQueryService extends QueryService<ProductFeatureApplType> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplTypeQueryService.class);

    private final ProductFeatureApplTypeRepository productFeatureApplTypeRepository;

    public ProductFeatureApplTypeQueryService(ProductFeatureApplTypeRepository productFeatureApplTypeRepository) {
        this.productFeatureApplTypeRepository = productFeatureApplTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductFeatureApplType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductFeatureApplType> findByCriteria(ProductFeatureApplTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductFeatureApplType> specification = createSpecification(criteria);
        return productFeatureApplTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureApplTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureApplType> specification = createSpecification(criteria);
        return productFeatureApplTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureApplTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureApplType> createSpecification(ProductFeatureApplTypeCriteria criteria) {
        Specification<ProductFeatureApplType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureApplType_.id),
                    buildStringSpecification(criteria.getName(), ProductFeatureApplType_.name),
                    buildStringSpecification(criteria.getDescription(), ProductFeatureApplType_.description)
                )
            );
        }
        return specification;
    }
}
