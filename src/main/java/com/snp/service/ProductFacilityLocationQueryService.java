package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFacilityLocation;
import com.snp.repository.ProductFacilityLocationRepository;
import com.snp.service.criteria.ProductFacilityLocationCriteria;
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
 * Service for executing complex queries for {@link ProductFacilityLocation} entities in the database.
 * The main input is a {@link ProductFacilityLocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFacilityLocation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFacilityLocationQueryService extends QueryService<ProductFacilityLocation> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityLocationQueryService.class);

    private final ProductFacilityLocationRepository productFacilityLocationRepository;

    public ProductFacilityLocationQueryService(ProductFacilityLocationRepository productFacilityLocationRepository) {
        this.productFacilityLocationRepository = productFacilityLocationRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFacilityLocation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFacilityLocation> findByCriteria(ProductFacilityLocationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFacilityLocation> specification = createSpecification(criteria);
        return productFacilityLocationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFacilityLocationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFacilityLocation> specification = createSpecification(criteria);
        return productFacilityLocationRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFacilityLocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFacilityLocation> createSpecification(ProductFacilityLocationCriteria criteria) {
        Specification<ProductFacilityLocation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFacilityLocation_.product, JoinType.LEFT);
                root.fetch(ProductFacilityLocation_.facility, JoinType.LEFT);
                root.fetch(ProductFacilityLocation_.facilityLocation, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFacilityLocation_.id),
                    buildRangeSpecification(criteria.getMinimumStock(), ProductFacilityLocation_.minimumStock),
                    buildRangeSpecification(criteria.getMoveQuantity(), ProductFacilityLocation_.moveQuantity),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(ProductFacilityLocation_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(ProductFacilityLocation_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFacilityLocationId(), root ->
                        root.join(ProductFacilityLocation_.facilityLocation, JoinType.LEFT).get(FacilityLocation_.id)
                    )
                )
            );
        }
        return specification;
    }
}
