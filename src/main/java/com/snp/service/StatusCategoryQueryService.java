package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.StatusCategory;
import com.snp.repository.StatusCategoryRepository;
import com.snp.service.criteria.StatusCategoryCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StatusCategory} entities in the database.
 * The main input is a {@link StatusCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StatusCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatusCategoryQueryService extends QueryService<StatusCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(StatusCategoryQueryService.class);

    private final StatusCategoryRepository statusCategoryRepository;

    public StatusCategoryQueryService(StatusCategoryRepository statusCategoryRepository) {
        this.statusCategoryRepository = statusCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link StatusCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StatusCategory> findByCriteria(StatusCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<StatusCategory> specification = createSpecification(criteria);
        return statusCategoryRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatusCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StatusCategory> specification = createSpecification(criteria);
        return statusCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link StatusCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StatusCategory> createSpecification(StatusCategoryCriteria criteria) {
        Specification<StatusCategory> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), StatusCategory_.id),
                    buildStringSpecification(criteria.getName(), StatusCategory_.name),
                    buildStringSpecification(criteria.getDescription(), StatusCategory_.description)
                )
            );
        }
        return specification;
    }
}
