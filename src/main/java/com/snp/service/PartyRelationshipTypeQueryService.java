package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyRelationshipType;
import com.snp.repository.PartyRelationshipTypeRepository;
import com.snp.service.criteria.PartyRelationshipTypeCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartyRelationshipType} entities in the database.
 * The main input is a {@link PartyRelationshipTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyRelationshipType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyRelationshipTypeQueryService extends QueryService<PartyRelationshipType> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipTypeQueryService.class);

    private final PartyRelationshipTypeRepository partyRelationshipTypeRepository;

    public PartyRelationshipTypeQueryService(PartyRelationshipTypeRepository partyRelationshipTypeRepository) {
        this.partyRelationshipTypeRepository = partyRelationshipTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PartyRelationshipType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyRelationshipType> findByCriteria(PartyRelationshipTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PartyRelationshipType> specification = createSpecification(criteria);
        return partyRelationshipTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyRelationshipTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyRelationshipType> specification = createSpecification(criteria);
        return partyRelationshipTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyRelationshipTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyRelationshipType> createSpecification(PartyRelationshipTypeCriteria criteria) {
        Specification<PartyRelationshipType> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyRelationshipType_.roleTypeFrom, JoinType.LEFT);
                root.fetch(PartyRelationshipType_.roleTypeTo, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyRelationshipType_.id),
                    buildStringSpecification(criteria.getName(), PartyRelationshipType_.name),
                    buildStringSpecification(criteria.getDescription(), PartyRelationshipType_.description),
                    buildSpecification(criteria.getRoleTypeFromId(), root ->
                        root.join(PartyRelationshipType_.roleTypeFrom, JoinType.LEFT).get(RoleType_.id)
                    ),
                    buildSpecification(criteria.getRoleTypeToId(), root ->
                        root.join(PartyRelationshipType_.roleTypeTo, JoinType.LEFT).get(RoleType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
