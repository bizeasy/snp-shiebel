package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ReturnHeaderType;
import com.snp.repository.ReturnHeaderTypeRepository;
import com.snp.service.criteria.ReturnHeaderTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ReturnHeaderType} entities in the database.
 * The main input is a {@link ReturnHeaderTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReturnHeaderType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnHeaderTypeQueryService extends QueryService<ReturnHeaderType> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderTypeQueryService.class);

    private final ReturnHeaderTypeRepository returnHeaderTypeRepository;

    public ReturnHeaderTypeQueryService(ReturnHeaderTypeRepository returnHeaderTypeRepository) {
        this.returnHeaderTypeRepository = returnHeaderTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ReturnHeaderType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReturnHeaderType> findByCriteria(ReturnHeaderTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ReturnHeaderType> specification = createSpecification(criteria);
        return returnHeaderTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnHeaderTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReturnHeaderType> specification = createSpecification(criteria);
        return returnHeaderTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ReturnHeaderTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReturnHeaderType> createSpecification(ReturnHeaderTypeCriteria criteria) {
        Specification<ReturnHeaderType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ReturnHeaderType_.id),
                    buildStringSpecification(criteria.getName(), ReturnHeaderType_.name),
                    buildStringSpecification(criteria.getDescription(), ReturnHeaderType_.description)
                )
            );
        }
        return specification;
    }
}
