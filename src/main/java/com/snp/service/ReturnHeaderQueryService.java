package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.ReturnHeader;
import com.snp.repository.ReturnHeaderRepository;
import com.snp.service.criteria.ReturnHeaderCriteria;
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
 * Service for executing complex queries for {@link ReturnHeader} entities in the database.
 * The main input is a {@link ReturnHeaderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReturnHeader} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReturnHeaderQueryService extends QueryService<ReturnHeader> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderQueryService.class);

    private final ReturnHeaderRepository returnHeaderRepository;

    public ReturnHeaderQueryService(ReturnHeaderRepository returnHeaderRepository) {
        this.returnHeaderRepository = returnHeaderRepository;
    }

    /**
     * Return a {@link Page} of {@link ReturnHeader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReturnHeader> findByCriteria(ReturnHeaderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReturnHeader> specification = createSpecification(criteria);
        return returnHeaderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReturnHeaderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReturnHeader> specification = createSpecification(criteria);
        return returnHeaderRepository.count(specification);
    }

    /**
     * Function to convert {@link ReturnHeaderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReturnHeader> createSpecification(ReturnHeaderCriteria criteria) {
        Specification<ReturnHeader> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ReturnHeader_.returnType, JoinType.LEFT);
                root.fetch(ReturnHeader_.status, JoinType.LEFT);
                root.fetch(ReturnHeader_.fromParty, JoinType.LEFT);
                root.fetch(ReturnHeader_.toParty, JoinType.LEFT);
                root.fetch(ReturnHeader_.paymentMethod, JoinType.LEFT);
                root.fetch(ReturnHeader_.originContactMech, JoinType.LEFT);
                root.fetch(ReturnHeader_.destinationFacility, JoinType.LEFT);
                root.fetch(ReturnHeader_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ReturnHeader_.id),
                    buildSpecification(criteria.getNeedsInventoryReceive(), ReturnHeader_.needsInventoryReceive),
                    buildStringSpecification(criteria.getSupplierRmaId(), ReturnHeader_.supplierRmaId),
                    buildSpecification(criteria.getReturnTypeId(), root ->
                        root.join(ReturnHeader_.returnType, JoinType.LEFT).get(ReturnHeaderType_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(ReturnHeader_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(ReturnHeader_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getToPartyId(), root -> root.join(ReturnHeader_.toParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getPaymentMethodId(), root ->
                        root.join(ReturnHeader_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)
                    ),
                    buildSpecification(criteria.getOriginContactMechId(), root ->
                        root.join(ReturnHeader_.originContactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getDestinationFacilityId(), root ->
                        root.join(ReturnHeader_.destinationFacility, JoinType.LEFT).get(Facility_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root ->
                        root.join(ReturnHeader_.currencyUom, JoinType.LEFT).get(Uom_.id)
                    )
                )
            );
        }
        return specification;
    }
}
