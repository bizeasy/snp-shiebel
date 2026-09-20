package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.QuoteType;
import com.snp.repository.QuoteTypeRepository;
import com.snp.service.criteria.QuoteTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link QuoteType} entities in the database.
 * The main input is a {@link QuoteTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuoteType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteTypeQueryService extends QueryService<QuoteType> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteTypeQueryService.class);

    private final QuoteTypeRepository quoteTypeRepository;

    public QuoteTypeQueryService(QuoteTypeRepository quoteTypeRepository) {
        this.quoteTypeRepository = quoteTypeRepository;
    }

    /**
     * Return a {@link List} of {@link QuoteType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuoteType> findByCriteria(QuoteTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<QuoteType> specification = createSpecification(criteria);
        return quoteTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuoteType> specification = createSpecification(criteria);
        return quoteTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuoteType> createSpecification(QuoteTypeCriteria criteria) {
        Specification<QuoteType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), QuoteType_.id),
                    buildStringSpecification(criteria.getName(), QuoteType_.name),
                    buildStringSpecification(criteria.getDescription(), QuoteType_.description)
                )
            );
        }
        return specification;
    }
}
