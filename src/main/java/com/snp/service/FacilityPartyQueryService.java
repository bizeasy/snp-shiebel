package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityParty;
import com.snp.repository.FacilityPartyRepository;
import com.snp.service.criteria.FacilityPartyCriteria;
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
 * Service for executing complex queries for {@link FacilityParty} entities in the database.
 * The main input is a {@link FacilityPartyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacilityParty} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityPartyQueryService extends QueryService<FacilityParty> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityPartyQueryService.class);

    private final FacilityPartyRepository facilityPartyRepository;

    public FacilityPartyQueryService(FacilityPartyRepository facilityPartyRepository) {
        this.facilityPartyRepository = facilityPartyRepository;
    }

    /**
     * Return a {@link Page} of {@link FacilityParty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityParty> findByCriteria(FacilityPartyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacilityParty> specification = createSpecification(criteria);
        return facilityPartyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityPartyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityParty> specification = createSpecification(criteria);
        return facilityPartyRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityPartyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityParty> createSpecification(FacilityPartyCriteria criteria) {
        Specification<FacilityParty> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityParty_.facility, JoinType.LEFT);
                root.fetch(FacilityParty_.party, JoinType.LEFT);
                root.fetch(FacilityParty_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityParty_.id),
                    buildRangeSpecification(criteria.getFromDate(), FacilityParty_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), FacilityParty_.thruDate),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(FacilityParty_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(FacilityParty_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root ->
                        root.join(FacilityParty_.roleType, JoinType.LEFT).get(RoleType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
