package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PaymentMethodType;
import com.snp.repository.PaymentMethodTypeRepository;
import com.snp.service.criteria.PaymentMethodTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentMethodType} entities in the database.
 * The main input is a {@link PaymentMethodTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethodType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodTypeQueryService extends QueryService<PaymentMethodType> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodTypeQueryService.class);

    private final PaymentMethodTypeRepository paymentMethodTypeRepository;

    public PaymentMethodTypeQueryService(PaymentMethodTypeRepository paymentMethodTypeRepository) {
        this.paymentMethodTypeRepository = paymentMethodTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentMethodType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodType> findByCriteria(PaymentMethodTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethodType> specification = createSpecification(criteria);
        return paymentMethodTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethodType> specification = createSpecification(criteria);
        return paymentMethodTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentMethodTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentMethodType> createSpecification(PaymentMethodTypeCriteria criteria) {
        Specification<PaymentMethodType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PaymentMethodType_.id),
                    buildStringSpecification(criteria.getName(), PaymentMethodType_.name),
                    buildStringSpecification(criteria.getDescription(), PaymentMethodType_.description)
                )
            );
        }
        return specification;
    }
}
