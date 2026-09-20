package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityLocation;
import com.snp.repository.FacilityLocationRepository;
import com.snp.service.criteria.FacilityLocationCriteria;
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
 * Service for executing complex queries for {@link FacilityLocation} entities in the database.
 * The main input is a {@link FacilityLocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacilityLocation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityLocationQueryService extends QueryService<FacilityLocation> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityLocationQueryService.class);

    private final FacilityLocationRepository facilityLocationRepository;

    public FacilityLocationQueryService(FacilityLocationRepository facilityLocationRepository) {
        this.facilityLocationRepository = facilityLocationRepository;
    }

    /**
     * Return a {@link Page} of {@link FacilityLocation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityLocation> findByCriteria(FacilityLocationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacilityLocation> specification = createSpecification(criteria);
        return facilityLocationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityLocationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityLocation> specification = createSpecification(criteria);
        return facilityLocationRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityLocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityLocation> createSpecification(FacilityLocationCriteria criteria) {
        Specification<FacilityLocation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityLocation_.facility, JoinType.LEFT);
                root.fetch(FacilityLocation_.locationType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityLocation_.id),
                    buildStringSpecification(criteria.getLocationSeqId(), FacilityLocation_.locationSeqId),
                    buildStringSpecification(criteria.getAreaId(), FacilityLocation_.areaId),
                    buildStringSpecification(criteria.getAisleId(), FacilityLocation_.aisleId),
                    buildStringSpecification(criteria.getSectionId(), FacilityLocation_.sectionId),
                    buildStringSpecification(criteria.getLevelId(), FacilityLocation_.levelId),
                    buildStringSpecification(criteria.getPositionId(), FacilityLocation_.positionId),
                    buildSpecification(criteria.getFacilityId(), root ->
                        root.join(FacilityLocation_.facility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getLocationTypeId(), root ->
                        root.join(FacilityLocation_.locationType, JoinType.LEFT).get(LocationType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
