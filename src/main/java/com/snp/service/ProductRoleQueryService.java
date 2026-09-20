package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ProductRole;
import com.snp.repository.ProductRoleRepository;
import com.snp.service.criteria.ProductRoleCriteria;
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
 * Service for executing complex queries for {@link ProductRole} entities in the database.
 * The main input is a {@link ProductRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductRole} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductRoleQueryService extends QueryService<ProductRole> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRoleQueryService.class);

    private final ProductRoleRepository productRoleRepository;

    public ProductRoleQueryService(ProductRoleRepository productRoleRepository) {
        this.productRoleRepository = productRoleRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductRole> findByCriteria(ProductRoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductRole> specification = createSpecification(criteria);
        return productRoleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductRole> specification = createSpecification(criteria);
        return productRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductRole> createSpecification(ProductRoleCriteria criteria) {
        Specification<ProductRole> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ProductRole_.product, JoinType.LEFT);
                root.fetch(ProductRole_.party, JoinType.LEFT);
                root.fetch(ProductRole_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ProductRole_.id),
                    buildSpecification(criteria.getProductId(), root -> root.join(ProductRole_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getPartyId(), root -> root.join(ProductRole_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root -> root.join(ProductRole_.roleType, JoinType.LEFT).get(RoleType_.id))
                )
            );
        }
        return specification;
    }
}
