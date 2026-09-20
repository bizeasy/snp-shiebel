package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.QuoteRole;
import com.snp.repository.QuoteRoleRepository;
import com.snp.service.criteria.QuoteRoleCriteria;
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
 * Service for executing complex queries for {@link QuoteRole} entities in the database.
 * The main input is a {@link QuoteRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuoteRole} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteRoleQueryService extends QueryService<QuoteRole> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteRoleQueryService.class);

    private final QuoteRoleRepository quoteRoleRepository;

    public QuoteRoleQueryService(QuoteRoleRepository quoteRoleRepository) {
        this.quoteRoleRepository = quoteRoleRepository;
    }

    /**
     * Return a {@link Page} of {@link QuoteRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteRole> findByCriteria(QuoteRoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuoteRole> specification = createSpecification(criteria);
        return quoteRoleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuoteRole> specification = createSpecification(criteria);
        return quoteRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuoteRole> createSpecification(QuoteRoleCriteria criteria) {
        Specification<QuoteRole> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(QuoteRole_.quote, JoinType.LEFT);
                root.fetch(QuoteRole_.quoteItem, JoinType.LEFT);
                root.fetch(QuoteRole_.party, JoinType.LEFT);
                root.fetch(QuoteRole_.roleType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), QuoteRole_.id),
                    buildSpecification(criteria.getQuoteId(), root -> root.join(QuoteRole_.quote, JoinType.LEFT).get(Quote_.id)),
                    buildSpecification(criteria.getQuoteItemId(), root ->
                        root.join(QuoteRole_.quoteItem, JoinType.LEFT).get(QuoteItem_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(QuoteRole_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root -> root.join(QuoteRole_.roleType, JoinType.LEFT).get(RoleType_.id))
                )
            );
        }
        return specification;
    }
}
