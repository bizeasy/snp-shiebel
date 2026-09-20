package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFeatureAppl;
import com.snp.repository.ProductFeatureApplRepository;
import com.snp.service.criteria.ProductFeatureApplCriteria;
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
 * Service for executing complex queries for {@link ProductFeatureAppl} entities in the database.
 * The main input is a {@link ProductFeatureApplCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFeatureAppl} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFeatureApplQueryService extends QueryService<ProductFeatureAppl> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplQueryService.class);

    private final ProductFeatureApplRepository productFeatureApplRepository;

    public ProductFeatureApplQueryService(ProductFeatureApplRepository productFeatureApplRepository) {
        this.productFeatureApplRepository = productFeatureApplRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFeatureAppl} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFeatureAppl> findByCriteria(ProductFeatureApplCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFeatureAppl> specification = createSpecification(criteria);
        return productFeatureApplRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFeatureApplCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFeatureAppl> specification = createSpecification(criteria);
        return productFeatureApplRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFeatureApplCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFeatureAppl> createSpecification(ProductFeatureApplCriteria criteria) {
        Specification<ProductFeatureAppl> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFeatureAppl_.product, JoinType.LEFT);
                root.fetch(ProductFeatureAppl_.productFeature, JoinType.LEFT);
                root.fetch(ProductFeatureAppl_.featureApplType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFeatureAppl_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductFeatureAppl_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductFeatureAppl_.thruDate),
                    buildRangeSpecification(criteria.getSequenceNo(), ProductFeatureAppl_.sequenceNo),
                    buildRangeSpecification(criteria.getAmount(), ProductFeatureAppl_.amount),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ProductFeatureAppl_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(ProductFeatureAppl_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getFeatureApplTypeId(), root ->
                        root.join(ProductFeatureAppl_.featureApplType, JoinType.LEFT).get(ProductFeatureApplType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
