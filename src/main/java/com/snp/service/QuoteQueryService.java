package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Quote;
import com.snp.repository.QuoteRepository;
import com.snp.service.criteria.QuoteCriteria;
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
 * Service for executing complex queries for {@link Quote} entities in the database.
 * The main input is a {@link QuoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Quote} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteQueryService extends QueryService<Quote> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteQueryService.class);

    private final QuoteRepository quoteRepository;

    public QuoteQueryService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    /**
     * Return a {@link Page} of {@link Quote} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Quote> findByCriteria(QuoteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quote> specification = createSpecification(criteria);
        return quoteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Quote> specification = createSpecification(criteria);
        return quoteRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quote> createSpecification(QuoteCriteria criteria) {
        Specification<Quote> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Quote_.quoteType, JoinType.LEFT);
                root.fetch(Quote_.custRequest, JoinType.LEFT);
                root.fetch(Quote_.party, JoinType.LEFT);
                root.fetch(Quote_.status, JoinType.LEFT);
                root.fetch(Quote_.productStore, JoinType.LEFT);
                root.fetch(Quote_.salesChannel, JoinType.LEFT);
                root.fetch(Quote_.currencyUom, JoinType.LEFT);
                root.fetch(Quote_.currencyConversion, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Quote_.id),
                    buildStringSpecification(criteria.getName(), Quote_.name),
                    buildStringSpecification(criteria.getOfferNo(), Quote_.offerNo),
                    buildRangeSpecification(criteria.getIssueDate(), Quote_.issueDate),
                    buildRangeSpecification(criteria.getValidFromDate(), Quote_.validFromDate),
                    buildRangeSpecification(criteria.getValidThruDate(), Quote_.validThruDate),
                    buildStringSpecification(criteria.getDescription(), Quote_.description),
                    buildRangeSpecification(criteria.getOfferValue(), Quote_.offerValue),
                    buildStringSpecification(criteria.getRemarks(), Quote_.remarks),
                    buildSpecification(criteria.getQuoteTypeId(), root -> root.join(Quote_.quoteType, JoinType.LEFT).get(QuoteType_.id)),
                    buildSpecification(criteria.getCustRequestId(), root ->
                        root.join(Quote_.custRequest, JoinType.LEFT).get(CustRequest_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(Quote_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Quote_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getProductStoreId(), root ->
                        root.join(Quote_.productStore, JoinType.LEFT).get(ProductStore_.id)
                    ),
                    buildSpecification(criteria.getSalesChannelId(), root ->
                        root.join(Quote_.salesChannel, JoinType.LEFT).get(SalesChannel_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(Quote_.currencyUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getCurrencyConversionId(), root ->
                        root.join(Quote_.currencyConversion, JoinType.LEFT).get(UomConversion_.id)
                    )
                )
            );
        }
        return specification;
    }
}
