package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityGroup;
import com.snp.repository.FacilityGroupRepository;
import com.snp.service.criteria.FacilityGroupCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FacilityGroup} entities in the database.
 * The main input is a {@link FacilityGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacilityGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityGroupQueryService extends QueryService<FacilityGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupQueryService.class);

    private final FacilityGroupRepository facilityGroupRepository;

    public FacilityGroupQueryService(FacilityGroupRepository facilityGroupRepository) {
        this.facilityGroupRepository = facilityGroupRepository;
    }

    /**
     * Return a {@link List} of {@link FacilityGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacilityGroup> findByCriteria(FacilityGroupCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<FacilityGroup> specification = createSpecification(criteria);
        return facilityGroupRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityGroup> specification = createSpecification(criteria);
        return facilityGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityGroup> createSpecification(FacilityGroupCriteria criteria) {
        Specification<FacilityGroup> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FacilityGroup_.facilityGroupType, JoinType.LEFT);
                root.fetch(FacilityGroup_.parentFacilityGroup, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityGroup_.id),
                    buildStringSpecification(criteria.getName(), FacilityGroup_.name),
                    buildStringSpecification(criteria.getDescription(), FacilityGroup_.description),
                    buildSpecification(criteria.getFacilityGroupTypeId(), root ->
                        root.join(FacilityGroup_.facilityGroupType, JoinType.LEFT).get(FacilityGroupType_.id)
                    ),
                    buildSpecification(criteria.getParentFacilityGroupId(), root ->
                        root.join(FacilityGroup_.parentFacilityGroup, JoinType.LEFT).get(FacilityGroup_.id)
                    )
                )
            );
        }
        return specification;
    }
}
