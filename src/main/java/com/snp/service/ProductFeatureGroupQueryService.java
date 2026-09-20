package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureGroup;
import com.snp.repository.ProductFeatureGroupRepository;
import com.snp.service.criteria.ProductFeatureGroupCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductFeatureGroup} entities in the database.
 * The main input is a {@link ProductFeatureGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductFeatureGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureGroupQueryService extends QueryService<ProductFeatureGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureGroupQueryService.class);

    private final ProductFeatureGroupRepository productFeatureGroupRepository;

    public ProductFeatureGroupQueryService(ProductFeatureGroupRepository productFeatureGroupRepository) {
        this.productFeatureGroupRepository = productFeatureGroupRepository;
    }

    /**
     * Return a {@link List} of {@link ProductFeatureGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductFeatureGroup> findByCriteria(ProductFeatureGroupCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductFeatureGroup> specification = createSpecification(criteria);
        return productFeatureGroupRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureGroup> specification = createSpecification(criteria);
        return productFeatureGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureGroup> createSpecification(ProductFeatureGroupCriteria criteria) {
        Specification<ProductFeatureGroup> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureGroup_.id),
                    buildStringSpecification(criteria.getName(), ProductFeatureGroup_.name),
                    buildStringSpecification(criteria.getDescription(), ProductFeatureGroup_.description)
                )
            );
        }
        return specification;
    }
}
