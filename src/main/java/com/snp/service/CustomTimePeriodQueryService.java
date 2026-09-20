package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustomTimePeriod;
import com.snp.repository.CustomTimePeriodRepository;
import com.snp.service.criteria.CustomTimePeriodCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustomTimePeriod} entities in the database.
 * The main input is a {@link CustomTimePeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomTimePeriod} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomTimePeriodQueryService extends QueryService<CustomTimePeriod> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomTimePeriodQueryService.class);

    private final CustomTimePeriodRepository customTimePeriodRepository;

    public CustomTimePeriodQueryService(CustomTimePeriodRepository customTimePeriodRepository) {
        this.customTimePeriodRepository = customTimePeriodRepository;
    }

    /**
     * Return a {@link List} of {@link CustomTimePeriod} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomTimePeriod> findByCriteria(CustomTimePeriodCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CustomTimePeriod> specification = createSpecification(criteria);
        return customTimePeriodRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomTimePeriodCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomTimePeriod> specification = createSpecification(criteria);
        return customTimePeriodRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomTimePeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomTimePeriod> createSpecification(CustomTimePeriodCriteria criteria) {
        Specification<CustomTimePeriod> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustomTimePeriod_.id),
                    buildStringSpecification(criteria.getName(), CustomTimePeriod_.name),
                    buildRangeSpecification(criteria.getFromDate(), CustomTimePeriod_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), CustomTimePeriod_.thruDate)
                )
            );
        }
        return specification;
    }
}
