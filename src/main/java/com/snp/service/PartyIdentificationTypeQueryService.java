package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyIdentificationType;
import com.snp.repository.PartyIdentificationTypeRepository;
import com.snp.service.criteria.PartyIdentificationTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartyIdentificationType} entities in the database.
 * The main input is a {@link PartyIdentificationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyIdentificationType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyIdentificationTypeQueryService extends QueryService<PartyIdentificationType> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationTypeQueryService.class);

    private final PartyIdentificationTypeRepository partyIdentificationTypeRepository;

    public PartyIdentificationTypeQueryService(PartyIdentificationTypeRepository partyIdentificationTypeRepository) {
        this.partyIdentificationTypeRepository = partyIdentificationTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PartyIdentificationType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyIdentificationType> findByCriteria(PartyIdentificationTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PartyIdentificationType> specification = createSpecification(criteria);
        return partyIdentificationTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyIdentificationTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyIdentificationType> specification = createSpecification(criteria);
        return partyIdentificationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyIdentificationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyIdentificationType> createSpecification(PartyIdentificationTypeCriteria criteria) {
        Specification<PartyIdentificationType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyIdentificationType_.id),
                    buildStringSpecification(criteria.getName(), PartyIdentificationType_.name),
                    buildStringSpecification(criteria.getDescription(), PartyIdentificationType_.description)
                )
            );
        }
        return specification;
    }
}
