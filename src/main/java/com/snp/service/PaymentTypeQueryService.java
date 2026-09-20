package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PaymentType;
import com.snp.repository.PaymentTypeRepository;
import com.snp.service.criteria.PaymentTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentType} entities in the database.
 * The main input is a {@link PaymentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentTypeQueryService extends QueryService<PaymentType> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentTypeQueryService.class);

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeQueryService(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentType> findByCriteria(PaymentTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<PaymentType> specification = createSpecification(criteria);
        return paymentTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentType> specification = createSpecification(criteria);
        return paymentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentType> createSpecification(PaymentTypeCriteria criteria) {
        Specification<PaymentType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PaymentType_.id),
                    buildStringSpecification(criteria.getName(), PaymentType_.name),
                    buildStringSpecification(criteria.getDescription(), PaymentType_.description)
                )
            );
        }
        return specification;
    }
}
