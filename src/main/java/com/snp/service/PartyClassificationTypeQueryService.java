package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyClassificationType;
import com.snp.repository.PartyClassificationTypeRepository;
import com.snp.service.criteria.PartyClassificationTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartyClassificationType} entities in the database.
 * The main input is a {@link PartyClassificationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyClassificationType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyClassificationTypeQueryService extends QueryService<PartyClassificationType> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationTypeQueryService.class);

    private final PartyClassificationTypeRepository partyClassificationTypeRepository;

    public PartyClassificationTypeQueryService(PartyClassificationTypeRepository partyClassificationTypeRepository) {
        this.partyClassificationTypeRepository = partyClassificationTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PartyClassificationType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyClassificationType> findByCriteria(PartyClassificationTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PartyClassificationType> specification = createSpecification(criteria);
        return partyClassificationTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyClassificationTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyClassificationType> specification = createSpecification(criteria);
        return partyClassificationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyClassificationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyClassificationType> createSpecification(PartyClassificationTypeCriteria criteria) {
        Specification<PartyClassificationType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyClassificationType_.id),
                    buildStringSpecification(criteria.getName(), PartyClassificationType_.name),
                    buildStringSpecification(criteria.getDescription(), PartyClassificationType_.description)
                )
            );
        }
        return specification;
    }
}
