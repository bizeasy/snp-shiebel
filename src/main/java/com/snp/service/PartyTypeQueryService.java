package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PartyType;
import com.snp.repository.PartyTypeRepository;
import com.snp.service.criteria.PartyTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PartyType} entities in the database.
 * The main input is a {@link PartyTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyTypeQueryService extends QueryService<PartyType> {

    private static final Logger LOG = LoggerFactory.getLogger(PartyTypeQueryService.class);

    private final PartyTypeRepository partyTypeRepository;

    public PartyTypeQueryService(PartyTypeRepository partyTypeRepository) {
        this.partyTypeRepository = partyTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PartyType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyType> findByCriteria(PartyTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PartyType> specification = createSpecification(criteria);
        return partyTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PartyType> specification = createSpecification(criteria);
        return partyTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyType> createSpecification(PartyTypeCriteria criteria) {
        Specification<PartyType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PartyType_.id),
                    buildStringSpecification(criteria.getName(), PartyType_.name),
                    buildStringSpecification(criteria.getDescription(), PartyType_.description)
                )
            );
        }
        return specification;
    }
}
