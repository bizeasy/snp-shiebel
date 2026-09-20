package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.StatusValidChange;
import com.snp.repository.StatusValidChangeRepository;
import com.snp.service.criteria.StatusValidChangeCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StatusValidChange} entities in the database.
 * The main input is a {@link StatusValidChangeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StatusValidChange} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatusValidChangeQueryService extends QueryService<StatusValidChange> {

    private static final Logger LOG = LoggerFactory.getLogger(StatusValidChangeQueryService.class);

    private final StatusValidChangeRepository statusValidChangeRepository;

    public StatusValidChangeQueryService(StatusValidChangeRepository statusValidChangeRepository) {
        this.statusValidChangeRepository = statusValidChangeRepository;
    }

    /**
     * Return a {@link List} of {@link StatusValidChange} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StatusValidChange> findByCriteria(StatusValidChangeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<StatusValidChange> specification = createSpecification(criteria);
        return statusValidChangeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatusValidChangeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StatusValidChange> specification = createSpecification(criteria);
        return statusValidChangeRepository.count(specification);
    }

    /**
     * Function to convert {@link StatusValidChangeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StatusValidChange> createSpecification(StatusValidChangeCriteria criteria) {
        Specification<StatusValidChange> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(StatusValidChange_.status, JoinType.LEFT);
                root.fetch(StatusValidChange_.statusTo, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), StatusValidChange_.id),
                    buildStringSpecification(criteria.getTransitionName(), StatusValidChange_.transitionName),
                    buildSpecification(criteria.getStatusId(), root -> root.join(StatusValidChange_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getStatusToId(), root ->
                        root.join(StatusValidChange_.statusTo, JoinType.LEFT).get(Status_.id)
                    )
                )
            );
        }
        return specification;
    }
}
