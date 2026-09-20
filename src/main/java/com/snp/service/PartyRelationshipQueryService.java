package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyRelationship;
import com.snp.repository.PartyRelationshipRepository;
import com.snp.service.criteria.PartyRelationshipCriteria;
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
 * Service for executing complex queries for {@link PartyRelationship} entities in the database.
 * The main input is a {@link PartyRelationshipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartyRelationship} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyRelationshipQueryService extends QueryService<PartyRelationship> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipQueryService.class);

    private final PartyRelationshipRepository partyRelationshipRepository;

    public PartyRelationshipQueryService(PartyRelationshipRepository partyRelationshipRepository) {
        this.partyRelationshipRepository = partyRelationshipRepository;
    }

    /**
     * Return a {@link Page} of {@link PartyRelationship} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyRelationship> findByCriteria(PartyRelationshipCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyRelationship> specification = createSpecification(criteria);
        return partyRelationshipRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyRelationshipCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyRelationship> specification = createSpecification(criteria);
        return partyRelationshipRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyRelationshipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyRelationship> createSpecification(PartyRelationshipCriteria criteria) {
        Specification<PartyRelationship> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyRelationship_.partyFrom, JoinType.LEFT);
                root.fetch(PartyRelationship_.partyTo, JoinType.LEFT);
                root.fetch(PartyRelationship_.roleTypeFrom, JoinType.LEFT);
                root.fetch(PartyRelationship_.roleTypeTo, JoinType.LEFT);
                root.fetch(PartyRelationship_.relationshipType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyRelationship_.id),
                    buildRangeSpecification(criteria.getFromDate(), PartyRelationship_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), PartyRelationship_.thruDate),
                    buildStringSpecification(criteria.getRelationshipName(), PartyRelationship_.relationshipName),
                    buildSpecification(criteria.getPartyFromId(), root ->
                        root.join(PartyRelationship_.partyFrom, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getPartyToId(), root ->
                        root.join(PartyRelationship_.partyTo, JoinType.LEFT).get(Party_.id)
                    ),
                    buildSpecification(criteria.getRoleTypeFromId(), root ->
                        root.join(PartyRelationship_.roleTypeFrom, JoinType.LEFT).get(RoleType_.id)
                    ),
                    buildSpecification(criteria.getRoleTypeToId(), root ->
                        root.join(PartyRelationship_.roleTypeTo, JoinType.LEFT).get(RoleType_.id)
                    ),
                    buildSpecification(criteria.getRelationshipTypeId(), root ->
                        root.join(PartyRelationship_.relationshipType, JoinType.LEFT).get(PartyRelationshipType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
