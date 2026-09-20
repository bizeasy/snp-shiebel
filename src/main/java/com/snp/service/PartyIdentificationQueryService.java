package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyIdentification;
import com.snp.repository.PartyIdentificationRepository;
import com.snp.service.criteria.PartyIdentificationCriteria;
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
 * Service for executing complex queries for {@link PartyIdentification} entities in the database.
 * The main input is a {@link PartyIdentificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PartyIdentification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyIdentificationQueryService extends QueryService<PartyIdentification> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationQueryService.class);

    private final PartyIdentificationRepository partyIdentificationRepository;

    public PartyIdentificationQueryService(PartyIdentificationRepository partyIdentificationRepository) {
        this.partyIdentificationRepository = partyIdentificationRepository;
    }

    /**
     * Return a {@link Page} of {@link PartyIdentification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyIdentification> findByCriteria(PartyIdentificationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyIdentification> specification = createSpecification(criteria);
        return partyIdentificationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyIdentificationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyIdentification> specification = createSpecification(criteria);
        return partyIdentificationRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyIdentificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyIdentification> createSpecification(PartyIdentificationCriteria criteria) {
        Specification<PartyIdentification> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PartyIdentification_.party, JoinType.LEFT);
                root.fetch(PartyIdentification_.type, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyIdentification_.id),
                    buildStringSpecification(criteria.getIdValue(), PartyIdentification_.idValue),
                    buildSpecification(criteria.getPartyId(), root -> root.join(PartyIdentification_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getTypeId(), root ->
                        root.join(PartyIdentification_.type, JoinType.LEFT).get(PartyIdentificationType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
