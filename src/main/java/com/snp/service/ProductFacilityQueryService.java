package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFacility;
import com.snp.repository.ProductFacilityRepository;
import com.snp.service.criteria.ProductFacilityCriteria;
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
 * Service for executing complex queries for {@link ProductFacility} entities in the database.
 * The main input is a {@link ProductFacilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFacility} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFacilityQueryService extends QueryService<ProductFacility> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityQueryService.class);

    private final ProductFacilityRepository productFacilityRepository;

    public ProductFacilityQueryService(ProductFacilityRepository productFacilityRepository) {
        this.productFacilityRepository = productFacilityRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFacility} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFacility> findByCriteria(ProductFacilityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFacility> specification = createSpecification(criteria);
        return productFacilityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFacilityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFacility> specification = createSpecification(criteria);
        return productFacilityRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFacilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFacility> createSpecification(ProductFacilityCriteria criteria) {
        Specification<ProductFacility> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFacility_.product, JoinType.LEFT);
                root.fetch(ProductFacility_.facility, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFacility_.id),
                    buildRangeSpecification(criteria.getMinimumStock(), ProductFacility_.minimumStock),
                    buildRangeSpecification(criteria.getReorderQty(), ProductFacility_.reorderQty),
                    buildRangeSpecification(criteria.getDaysToShip(), ProductFacility_.daysToShip),
                    buildRangeSpecification(criteria.getLastInventoryCount(), ProductFacility_.lastInventoryCount),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ProductFacility_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(ProductFacility_.facility, JoinType.LEFT).get(Facility_.id)
                    )
                )
            );
        }
        return specification;
    }
}
