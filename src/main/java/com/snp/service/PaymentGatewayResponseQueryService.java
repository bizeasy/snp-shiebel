package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PaymentGatewayResponse;
import com.snp.repository.PaymentGatewayResponseRepository;
import com.snp.service.criteria.PaymentGatewayResponseCriteria;
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
 * Service for executing complex queries for {@link PaymentGatewayResponse} entities in the database.
 * The main input is a {@link PaymentGatewayResponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentGatewayResponse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentGatewayResponseQueryService extends QueryService<PaymentGatewayResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayResponseQueryService.class);

    private final PaymentGatewayResponseRepository paymentGatewayResponseRepository;

    public PaymentGatewayResponseQueryService(PaymentGatewayResponseRepository paymentGatewayResponseRepository) {
        this.paymentGatewayResponseRepository = paymentGatewayResponseRepository;
    }

    /**
     * Return a {@link Page} of {@link PaymentGatewayResponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentGatewayResponse> findByCriteria(PaymentGatewayResponseCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentGatewayResponse> specification = createSpecification(criteria);
        return paymentGatewayResponseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentGatewayResponseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentGatewayResponse> specification = createSpecification(criteria);
        return paymentGatewayResponseRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentGatewayResponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentGatewayResponse> createSpecification(PaymentGatewayResponseCriteria criteria) {
        Specification<PaymentGatewayResponse> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PaymentGatewayResponse_.paymentGatewayConfig, JoinType.LEFT);
                root.fetch(PaymentGatewayResponse_.paymentMethodType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PaymentGatewayResponse_.id),
                    buildStringSpecification(criteria.getReferenceNumber(), PaymentGatewayResponse_.referenceNumber),
                    buildRangeSpecification(criteria.getAmount(), PaymentGatewayResponse_.amount),
                    buildStringSpecification(criteria.getGatewayMessage(), PaymentGatewayResponse_.gatewayMessage),
                    buildSpecification(criteria.getPaymentGatewayConfigId(), root ->
                        root.join(PaymentGatewayResponse_.paymentGatewayConfig, JoinType.LEFT).get(PaymentGatewayConfig_.id)
                    ),
                    buildSpecification(criteria.getPaymentMethodTypeId(), root ->
                        root.join(PaymentGatewayResponse_.paymentMethodType, JoinType.LEFT).get(PaymentMethodType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
