package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyClassificationGroup;
import com.snp.repository.PartyClassificationGroupRepository;
import com.snp.service.criteria.PartyClassificationGroupCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartyClassificationGroup} entities in the database.
 * The main input is a {@link PartyClassificationGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyClassificationGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyClassificationGroupQueryService extends QueryService<PartyClassificationGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationGroupQueryService.class);

    private final PartyClassificationGroupRepository partyClassificationGroupRepository;

    public PartyClassificationGroupQueryService(PartyClassificationGroupRepository partyClassificationGroupRepository) {
        this.partyClassificationGroupRepository = partyClassificationGroupRepository;
    }

    /**
     * Return a {@link List} of {@link PartyClassificationGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyClassificationGroup> findByCriteria(PartyClassificationGroupCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PartyClassificationGroup> specification = createSpecification(criteria);
        return partyClassificationGroupRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyClassificationGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyClassificationGroup> specification = createSpecification(criteria);
        return partyClassificationGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyClassificationGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyClassificationGroup> createSpecification(PartyClassificationGroupCriteria criteria) {
        Specification<PartyClassificationGroup> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyClassificationGroup_.classificationType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyClassificationGroup_.id),
                    buildStringSpecification(criteria.getName(), PartyClassificationGroup_.name),
                    buildStringSpecification(criteria.getDescription(), PartyClassificationGroup_.description),
                    buildSpecification(criteria.getClassificationTypeId(), root ->
                        root.join(PartyClassificationGroup_.classificationType, JoinType.LEFT).get(PartyClassificationType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
