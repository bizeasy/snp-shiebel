package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Payment;
import com.snp.repository.PaymentRepository;
import com.snp.service.criteria.PaymentCriteria;
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
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Payment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    public PaymentQueryService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Return a {@link Page} of {@link Payment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Payment> findByCriteria(PaymentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Payment_.paymentType, JoinType.LEFT);
                root.fetch(Payment_.paymentMethodType, JoinType.LEFT);
                root.fetch(Payment_.paymentMethod, JoinType.LEFT);
                root.fetch(Payment_.paymentGatewayResponse, JoinType.LEFT);
                root.fetch(Payment_.status, JoinType.LEFT);
                root.fetch(Payment_.fromParty, JoinType.LEFT);
                root.fetch(Payment_.toParty, JoinType.LEFT);
                root.fetch(Payment_.roleType, JoinType.LEFT);
                root.fetch(Payment_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Payment_.id),
                    buildStringSpecification(criteria.getPaymentRefNumber(), Payment_.paymentRefNumber),
                    buildRangeSpecification(criteria.getEffectiveDate(), Payment_.effectiveDate),
                    buildRangeSpecification(criteria.getPaymentDate(), Payment_.paymentDate),
                    buildRangeSpecification(criteria.getAmount(), Payment_.amount),
                    buildRangeSpecification(criteria.getActualAmount(), Payment_.actualAmount),
                    buildStringSpecification(criteria.getPaymentStatus(), Payment_.paymentStatus),
                    buildStringSpecification(criteria.getMihpayId(), Payment_.mihpayId),
                    buildStringSpecification(criteria.getTxnId(), Payment_.txnId),
                    buildStringSpecification(criteria.getEmail(), Payment_.email),
                    buildStringSpecification(criteria.getPhone(), Payment_.phone),
                    buildStringSpecification(criteria.getProductInfo(), Payment_.productInfo),
                    buildSpecification(criteria.getPaymentTypeId(), root ->
                        root.join(Payment_.paymentType, JoinType.LEFT).get(PaymentType_.id)
                    ),
                    buildSpecification(criteria.getPaymentMethodTypeId(), root ->
                        root.join(Payment_.paymentMethodType, JoinType.LEFT).get(PaymentMethodType_.id)
                    ),
                    buildSpecification(criteria.getPaymentMethodId(), root ->
                        root.join(Payment_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)
                    ),
                    buildSpecification(criteria.getPaymentGatewayResponseId(), root ->
                        root.join(Payment_.paymentGatewayResponse, JoinType.LEFT).get(PaymentGatewayResponse_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Payment_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(Payment_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getToPartyId(), root -> root.join(Payment_.toParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root -> root.join(Payment_.roleType, JoinType.LEFT).get(RoleType_.id)),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(Payment_.currencyUom, JoinType.LEFT).get(Uom_.id))
                )
            );
        }
        return specification;
    }
}
