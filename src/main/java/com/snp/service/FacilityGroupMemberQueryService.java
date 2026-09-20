package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityGroupMember;
import com.snp.repository.FacilityGroupMemberRepository;
import com.snp.service.criteria.FacilityGroupMemberCriteria;
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
 * Service for executing complex queries for {@link FacilityGroupMember} entities in the database.
 * The main input is a {@link FacilityGroupMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacilityGroupMember} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityGroupMemberQueryService extends QueryService<FacilityGroupMember> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupMemberQueryService.class);

    private final FacilityGroupMemberRepository facilityGroupMemberRepository;

    public FacilityGroupMemberQueryService(FacilityGroupMemberRepository facilityGroupMemberRepository) {
        this.facilityGroupMemberRepository = facilityGroupMemberRepository;
    }

    /**
     * Return a {@link Page} of {@link FacilityGroupMember} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityGroupMember> findByCriteria(FacilityGroupMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacilityGroupMember> specification = createSpecification(criteria);
        return facilityGroupMemberRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityGroupMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityGroupMember> specification = createSpecification(criteria);
        return facilityGroupMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityGroupMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityGroupMember> createSpecification(FacilityGroupMemberCriteria criteria) {
        Specification<FacilityGroupMember> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityGroupMember_.facility, JoinType.LEFT);
                root.fetch(FacilityGroupMember_.facilityGroup, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityGroupMember_.id),
                    buildRangeSpecification(criteria.getFromDate(), FacilityGroupMember_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), FacilityGroupMember_.thruDate),
                    buildRangeSpecification(criteria.getSequenceNo(), FacilityGroupMember_.sequenceNo),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(FacilityGroupMember_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getFacilityGroupId(), root ->
                        root.join(FacilityGroupMember_.facilityGroup, JoinType.LEFT).get(FacilityGroup_.id)
                    )
                )
            );
        }
        return specification;
    }
}
