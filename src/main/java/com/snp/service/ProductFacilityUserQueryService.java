package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductFacilityUser;
import com.snp.repository.ProductFacilityUserRepository;
import com.snp.service.criteria.ProductFacilityUserCriteria;
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
 * Service for executing complex queries for {@link ProductFacilityUser} entities in the database.
 * The main input is a {@link ProductFacilityUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductFacilityUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductFacilityUserQueryService extends QueryService<ProductFacilityUser> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFacilityUserQueryService.class);

    private final ProductFacilityUserRepository productFacilityUserRepository;

    public ProductFacilityUserQueryService(ProductFacilityUserRepository productFacilityUserRepository) {
        this.productFacilityUserRepository = productFacilityUserRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductFacilityUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductFacilityUser> findByCriteria(ProductFacilityUserCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductFacilityUser> specification = createSpecification(criteria);
        return productFacilityUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductFacilityUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductFacilityUser> specification = createSpecification(criteria);
        return productFacilityUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductFacilityUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductFacilityUser> createSpecification(ProductFacilityUserCriteria criteria) {
        Specification<ProductFacilityUser> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductFacilityUser_.productFacility, JoinType.LEFT);
                root.fetch(ProductFacilityUser_.party, JoinType.LEFT);
                root.fetch(ProductFacilityUser_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductFacilityUser_.id),
                    buildRangeSpecification(criteria.getFromDate(), ProductFacilityUser_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), ProductFacilityUser_.thruDate),
                    buildRangeSpecification(criteria.getCostPerDay(), ProductFacilityUser_.costPerDay),
                    buildRangeSpecification(criteria.getTotalCost(), ProductFacilityUser_.totalCost),
                    buildRangeSpecification(criteria.getDiscount(), ProductFacilityUser_.discount),
                    buildSpecification(criteria.getProductFacilityId(), root ->
                        root.join(ProductFacilityUser_.productFacility, JoinType.LEFT).get(ProductFacility_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(ProductFacilityUser_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getStatusId(), root ->
                        root.join(ProductFacilityUser_.status, JoinType.LEFT).get(Status_.id)
                    )
                )
            );
        }
        return specification;
    }
}
