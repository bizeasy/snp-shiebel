package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.FacilityGroupType;
import com.snp.repository.FacilityGroupTypeRepository;
import com.snp.service.criteria.FacilityGroupTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FacilityGroupType} entities in the database.
 * The main input is a {@link FacilityGroupTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacilityGroupType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityGroupTypeQueryService extends QueryService<FacilityGroupType> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupTypeQueryService.class);

    private final FacilityGroupTypeRepository facilityGroupTypeRepository;

    public FacilityGroupTypeQueryService(FacilityGroupTypeRepository facilityGroupTypeRepository) {
        this.facilityGroupTypeRepository = facilityGroupTypeRepository;
    }

    /**
     * Return a {@link List} of {@link FacilityGroupType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacilityGroupType> findByCriteria(FacilityGroupTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<FacilityGroupType> specification = createSpecification(criteria);
        return facilityGroupTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityGroupTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityGroupType> specification = createSpecification(criteria);
        return facilityGroupTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityGroupTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityGroupType> createSpecification(FacilityGroupTypeCriteria criteria) {
        Specification<FacilityGroupType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FacilityGroupType_.id),
                    buildStringSpecification(criteria.getName(), FacilityGroupType_.name),
                    buildStringSpecification(criteria.getDescription(), FacilityGroupType_.description)
                )
            );
        }
        return specification;
    }
}
