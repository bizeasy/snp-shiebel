package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyContactMech;
import com.snp.repository.PartyContactMechRepository;
import com.snp.service.criteria.PartyContactMechCriteria;
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
 * Service for executing complex queries for {@link PartyContactMech} entities in the database.
 * The main input is a {@link PartyContactMechCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartyContactMech} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyContactMechQueryService extends QueryService<PartyContactMech> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyContactMechQueryService.class);

    private final PartyContactMechRepository partyContactMechRepository;

    public PartyContactMechQueryService(PartyContactMechRepository partyContactMechRepository) {
        this.partyContactMechRepository = partyContactMechRepository;
    }

    /**
     * Return a {@link Page} of {@link PartyContactMech} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyContactMech> findByCriteria(PartyContactMechCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyContactMech> specification = createSpecification(criteria);
        return partyContactMechRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyContactMechCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyContactMech> specification = createSpecification(criteria);
        return partyContactMechRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyContactMechCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyContactMech> createSpecification(PartyContactMechCriteria criteria) {
        Specification<PartyContactMech> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyContactMech_.party, JoinType.LEFT);
                root.fetch(PartyContactMech_.contactMech, JoinType.LEFT);
                root.fetch(PartyContactMech_.contactMechPurpose, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyContactMech_.id),
                    buildRangeSpecification(criteria.getFromDate(), PartyContactMech_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), PartyContactMech_.thruDate),
                    buildSpecification(criteria.getPartyId(), root -> root.join(PartyContactMech_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(PartyContactMech_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getContactMechPurposeId(), root ->
                        root.join(PartyContactMech_.contactMechPurpose, JoinType.LEFT).get(ContactMechPurpose_.id)
                    )
                )
            );
        }
        return specification;
    }
}
