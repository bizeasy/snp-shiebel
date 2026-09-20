package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureType;
import com.snp.repository.ProductFeatureTypeRepository;
import com.snp.service.criteria.ProductFeatureTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductFeatureType} entities in the database.
 * The main input is a {@link ProductFeatureTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductFeatureType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureTypeQueryService extends QueryService<ProductFeatureType> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureTypeQueryService.class);

    private final ProductFeatureTypeRepository productFeatureTypeRepository;

    public ProductFeatureTypeQueryService(ProductFeatureTypeRepository productFeatureTypeRepository) {
        this.productFeatureTypeRepository = productFeatureTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ProductFeatureType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductFeatureType> findByCriteria(ProductFeatureTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductFeatureType> specification = createSpecification(criteria);
        return productFeatureTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureType> specification = createSpecification(criteria);
        return productFeatureTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureType> createSpecification(ProductFeatureTypeCriteria criteria) {
        Specification<ProductFeatureType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureType_.id),
                    buildStringSpecification(criteria.getName(), ProductFeatureType_.name),
                    buildStringSpecification(criteria.getDescription(), ProductFeatureType_.description)
                )
            );
        }
        return specification;
    }
}
