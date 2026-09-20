package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Party;
import com.snp.repository.PartyRepository;
import com.snp.service.criteria.PartyCriteria;
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
 * Service for executing complex queries for {@link Party} entities in the database.
 * The main input is a {@link PartyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Party} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyQueryService extends QueryService<Party> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyQueryService.class);

    private final PartyRepository partyRepository;

    public PartyQueryService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    /**
     * Return a {@link Page} of {@link Party} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Party> findByCriteria(PartyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Party> specification = createSpecification(criteria);
        return partyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Party> specification = createSpecification(criteria);
        return partyRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Party> createSpecification(PartyCriteria criteria) {
        Specification<Party> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Party_.user, JoinType.LEFT);
                root.fetch(Party_.partyType, JoinType.LEFT);
                root.fetch(Party_.status, JoinType.LEFT);
                root.fetch(Party_.address, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Party_.id),
                    buildSpecification(criteria.getIsOrganisation(), Party_.isOrganisation),
                    buildStringSpecification(criteria.getOrganisationName(), Party_.organisationName),
                    buildStringSpecification(criteria.getOrganisationShortName(), Party_.organisationShortName),
                    buildStringSpecification(criteria.getSalutation(), Party_.salutation),
                    buildStringSpecification(criteria.getFirstName(), Party_.firstName),
                    buildStringSpecification(criteria.getMiddleName(), Party_.middleName),
                    buildStringSpecification(criteria.getLastName(), Party_.lastName),
                    buildSpecification(criteria.getGender(), Party_.gender),
                    buildRangeSpecification(criteria.getBirthDate(), Party_.birthDate),
                    buildStringSpecification(criteria.getPrimaryPhone(), Party_.primaryPhone),
                    buildStringSpecification(criteria.getPrimaryEmail(), Party_.primaryEmail),
                    buildSpecification(criteria.getIsTemporaryPassword(), Party_.isTemporaryPassword),
                    buildStringSpecification(criteria.getLogoImageUrl(), Party_.logoImageUrl),
                    buildStringSpecification(criteria.getProfileImageUrl(), Party_.profileImageUrl),
                    buildSpecification(criteria.getUserId(), root -> root.join(Party_.user, JoinType.LEFT).get(User_.id)),
                    buildSpecification(criteria.getPartyTypeId(), root -> root.join(Party_.partyType, JoinType.LEFT).get(PartyType_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Party_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getAddressId(), root -> root.join(Party_.address, JoinType.LEFT).get(PostalAddress_.id))
                )
            );
        }
        return specification;
    }
}
