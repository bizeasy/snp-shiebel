package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequest;
import com.snp.repository.CustRequestRepository;
import com.snp.service.criteria.CustRequestCriteria;
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
 * Service for executing complex queries for {@link CustRequest} entities in the database.
 * The main input is a {@link CustRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestQueryService extends QueryService<CustRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestQueryService.class);

    private final CustRequestRepository custRequestRepository;

    public CustRequestQueryService(CustRequestRepository custRequestRepository) {
        this.custRequestRepository = custRequestRepository;
    }

    /**
     * Return a {@link Page} of {@link CustRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustRequest> findByCriteria(CustRequestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustRequest> specification = createSpecification(criteria);
        return custRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequest> specification = createSpecification(criteria);
        return custRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequest> createSpecification(CustRequestCriteria criteria) {
        Specification<CustRequest> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequest_.custRequestType, JoinType.LEFT);
                root.fetch(CustRequest_.custRequestCategory, JoinType.LEFT);
                root.fetch(CustRequest_.status, JoinType.LEFT);
                root.fetch(CustRequest_.fromParty, JoinType.LEFT);
                root.fetch(CustRequest_.productStore, JoinType.LEFT);
                root.fetch(CustRequest_.salesChannel, JoinType.LEFT);
                root.fetch(CustRequest_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequest_.id),
                    buildStringSpecification(criteria.getName(), CustRequest_.name),
                    buildStringSpecification(criteria.getDescription(), CustRequest_.description),
                    buildRangeSpecification(criteria.getPriority(), CustRequest_.priority),
                    buildRangeSpecification(criteria.getCustRequestDate(), CustRequest_.custRequestDate),
                    buildRangeSpecification(criteria.getResponseRequiredDate(), CustRequest_.responseRequiredDate),
                    buildRangeSpecification(criteria.getOpenDateTime(), CustRequest_.openDateTime),
                    buildRangeSpecification(criteria.getClosedDateTime(), CustRequest_.closedDateTime),
                    buildStringSpecification(criteria.getInternalComment(), CustRequest_.internalComment),
                    buildSpecification(criteria.getBilled(), CustRequest_.billed),
                    buildRangeSpecification(criteria.getCustEstimatedDurationDays(), CustRequest_.custEstimatedDurationDays),
                    buildSpecification(criteria.getCustRequestTypeId(), root ->
                        root.join(CustRequest_.custRequestType, JoinType.LEFT).get(CustRequestType_.id)
                    ),
                    buildSpecification(criteria.getCustRequestCategoryId(), root ->
                        root.join(CustRequest_.custRequestCategory, JoinType.LEFT).get(CustRequestCategory_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(CustRequest_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(CustRequest_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getProductStoreId(), root ->
                        root.join(CustRequest_.productStore, JoinType.LEFT).get(ProductStore_.id)
                    ),
                    buildSpecification(criteria.getSalesChannelId(), root ->
                        root.join(CustRequest_.salesChannel, JoinType.LEFT).get(SalesChannel_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(CustRequest_.currencyUom, JoinType.LEFT).get(Uom_.id))
                )
            );
        }
        return specification;
    }
}
