package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.DepartmentMember;
import com.snp.repository.DepartmentMemberRepository;
import com.snp.service.criteria.DepartmentMemberCriteria;
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
 * Service for executing complex queries for {@link DepartmentMember} entities in the database.
 * The main input is a {@link DepartmentMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DepartmentMember} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentMemberQueryService extends QueryService<DepartmentMember> {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentMemberQueryService.class);

    private final DepartmentMemberRepository departmentMemberRepository;

    public DepartmentMemberQueryService(DepartmentMemberRepository departmentMemberRepository) {
        this.departmentMemberRepository = departmentMemberRepository;
    }

    /**
     * Return a {@link Page} of {@link DepartmentMember} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartmentMember> findByCriteria(DepartmentMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepartmentMember> specification = createSpecification(criteria);
        return departmentMemberRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DepartmentMember> specification = createSpecification(criteria);
        return departmentMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepartmentMember> createSpecification(DepartmentMemberCriteria criteria) {
        Specification<DepartmentMember> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(DepartmentMember_.department, JoinType.LEFT);
                root.fetch(DepartmentMember_.party, JoinType.LEFT);
                root.fetch(DepartmentMember_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), DepartmentMember_.id),
                    buildRangeSpecification(criteria.getFromDate(), DepartmentMember_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), DepartmentMember_.thruDate),
                    buildSpecification(criteria.getIsPrimary(), DepartmentMember_.isPrimary),
                    buildSpecification(criteria.getDepartmentId(), root ->
                        root.join(DepartmentMember_.department, JoinType.LEFT).get(Department_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(DepartmentMember_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root ->
                        root.join(DepartmentMember_.roleType, JoinType.LEFT).get(RoleType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
