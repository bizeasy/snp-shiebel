package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PaymentGatewayConfig;
import com.snp.repository.PaymentGatewayConfigRepository;
import com.snp.service.criteria.PaymentGatewayConfigCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentGatewayConfig} entities in the database.
 * The main input is a {@link PaymentGatewayConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentGatewayConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentGatewayConfigQueryService extends QueryService<PaymentGatewayConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayConfigQueryService.class);

    private final PaymentGatewayConfigRepository paymentGatewayConfigRepository;

    public PaymentGatewayConfigQueryService(PaymentGatewayConfigRepository paymentGatewayConfigRepository) {
        this.paymentGatewayConfigRepository = paymentGatewayConfigRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentGatewayConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentGatewayConfig> findByCriteria(PaymentGatewayConfigCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PaymentGatewayConfig> specification = createSpecification(criteria);
        return paymentGatewayConfigRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentGatewayConfigCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentGatewayConfig> specification = createSpecification(criteria);
        return paymentGatewayConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentGatewayConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentGatewayConfig> createSpecification(PaymentGatewayConfigCriteria criteria) {
        Specification<PaymentGatewayConfig> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PaymentGatewayConfig_.id),
                    buildStringSpecification(criteria.getName(), PaymentGatewayConfig_.name),
                    buildStringSpecification(criteria.getAuthUrl(), PaymentGatewayConfig_.authUrl),
                    buildStringSpecification(criteria.getReleaseUrl(), PaymentGatewayConfig_.releaseUrl),
                    buildStringSpecification(criteria.getRefundUrl(), PaymentGatewayConfig_.refundUrl)
                )
            );
        }
        return specification;
    }
}
