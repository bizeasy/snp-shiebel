package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Status;
import com.snp.repository.StatusRepository;
import com.snp.service.criteria.StatusCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Status} entities in the database.
 * The main input is a {@link StatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Status} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatusQueryService extends QueryService<Status> {

    private static final Logger LOG = LoggerFactory.getLogger(StatusQueryService.class);

    private final StatusRepository statusRepository;

    public StatusQueryService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Return a {@link List} of {@link Status} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Status> findByCriteria(StatusCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Status> specification = createSpecification(criteria);
        return statusRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Status> specification = createSpecification(criteria);
        return statusRepository.count(specification);
    }

    /**
     * Function to convert {@link StatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Status> createSpecification(StatusCriteria criteria) {
        Specification<Status> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Status_.category, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Status_.id),
                    buildStringSpecification(criteria.getName(), Status_.name),
                    buildRangeSpecification(criteria.getSequenceNo(), Status_.sequenceNo),
                    buildStringSpecification(criteria.getDescription(), Status_.description),
                    buildSpecification(criteria.getCategoryId(), root -> root.join(Status_.category, JoinType.LEFT).get(StatusCategory_.id))
                )
            );
        }
        return specification;
    }
}
