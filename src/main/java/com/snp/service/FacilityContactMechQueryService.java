package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityContactMech;
import com.snp.repository.FacilityContactMechRepository;
import com.snp.service.criteria.FacilityContactMechCriteria;
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
 * Service for executing complex queries for {@link FacilityContactMech} entities in the database.
 * The main input is a {@link FacilityContactMechCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacilityContactMech} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityContactMechQueryService extends QueryService<FacilityContactMech> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityContactMechQueryService.class);

    private final FacilityContactMechRepository facilityContactMechRepository;

    public FacilityContactMechQueryService(FacilityContactMechRepository facilityContactMechRepository) {
        this.facilityContactMechRepository = facilityContactMechRepository;
    }

    /**
     * Return a {@link Page} of {@link FacilityContactMech} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityContactMech> findByCriteria(FacilityContactMechCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacilityContactMech> specification = createSpecification(criteria);
        return facilityContactMechRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityContactMechCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityContactMech> specification = createSpecification(criteria);
        return facilityContactMechRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityContactMechCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityContactMech> createSpecification(FacilityContactMechCriteria criteria) {
        Specification<FacilityContactMech> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityContactMech_.facility, JoinType.LEFT);
                root.fetch(FacilityContactMech_.contactMech, JoinType.LEFT);
                root.fetch(FacilityContactMech_.contactMechPurpose, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityContactMech_.id),
                    buildRangeSpecification(criteria.getFromDate(), FacilityContactMech_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), FacilityContactMech_.thruDate),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(FacilityContactMech_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(FacilityContactMech_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getContactMechPurposeId(), root ->
                        root.join(FacilityContactMech_.contactMechPurpose, JoinType.LEFT).get(ContactMechPurpose_.id)
                    )
                )
            );
        }
        return specification;
    }
}
