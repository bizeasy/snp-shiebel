package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyClassification;
import com.snp.repository.PartyClassificationRepository;
import com.snp.service.criteria.PartyClassificationCriteria;
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
 * Service for executing complex queries for {@link PartyClassification} entities in the database.
 * The main input is a {@link PartyClassificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartyClassification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyClassificationQueryService extends QueryService<PartyClassification> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationQueryService.class);

    private final PartyClassificationRepository partyClassificationRepository;

    public PartyClassificationQueryService(PartyClassificationRepository partyClassificationRepository) {
        this.partyClassificationRepository = partyClassificationRepository;
    }

    /**
     * Return a {@link Page} of {@link PartyClassification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyClassification> findByCriteria(PartyClassificationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyClassification> specification = createSpecification(criteria);
        return partyClassificationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyClassificationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyClassification> specification = createSpecification(criteria);
        return partyClassificationRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyClassificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyClassification> createSpecification(PartyClassificationCriteria criteria) {
        Specification<PartyClassification> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyClassification_.party, JoinType.LEFT);
                root.fetch(PartyClassification_.classificationGroup, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyClassification_.id),
                    buildRangeSpecification(criteria.getFromDate(), PartyClassification_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), PartyClassification_.thruDate),
                    buildSpecification(criteria.getPartyId(), root -> root.join(PartyClassification_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getClassificationGroupId(), root ->
                        root.join(PartyClassification_.classificationGroup, JoinType.LEFT).get(PartyClassificationGroup_.id)
                    )
                )
            );
        }
        return specification;
    }
}
