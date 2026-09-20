package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestRole;
import com.snp.repository.CustRequestRoleRepository;
import com.snp.service.criteria.CustRequestRoleCriteria;
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
 * Service for executing complex queries for {@link CustRequestRole} entities in the database.
 * The main input is a {@link CustRequestRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustRequestRole} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestRoleQueryService extends QueryService<CustRequestRole> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestRoleQueryService.class);

    private final CustRequestRoleRepository custRequestRoleRepository;

    public CustRequestRoleQueryService(CustRequestRoleRepository custRequestRoleRepository) {
        this.custRequestRoleRepository = custRequestRoleRepository;
    }

    /**
     * Return a {@link Page} of {@link CustRequestRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustRequestRole> findByCriteria(CustRequestRoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustRequestRole> specification = createSpecification(criteria);
        return custRequestRoleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestRole> specification = createSpecification(criteria);
        return custRequestRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestRole> createSpecification(CustRequestRoleCriteria criteria) {
        Specification<CustRequestRole> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestRole_.custRequest, JoinType.LEFT);
                root.fetch(CustRequestRole_.custRequestItem, JoinType.LEFT);
                root.fetch(CustRequestRole_.party, JoinType.LEFT);
                root.fetch(CustRequestRole_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestRole_.id),
                    buildSpecification(criteria.getCustRequestId(), root ->
                        root.join(CustRequestRole_.custRequest, JoinType.LEFT).get(CustRequest_.id)
                    ),
                    buildSpecification(criteria.getCustRequestItemId(), root ->
                        root.join(CustRequestRole_.custRequestItem, JoinType.LEFT).get(CustRequestItem_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(CustRequestRole_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root ->
                        root.join(CustRequestRole_.roleType, JoinType.LEFT).get(RoleType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
