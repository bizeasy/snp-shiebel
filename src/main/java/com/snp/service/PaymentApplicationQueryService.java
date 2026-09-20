package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PaymentApplication;
import com.snp.repository.PaymentApplicationRepository;
import com.snp.service.criteria.PaymentApplicationCriteria;
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
 * Service for executing complex queries for {@link PaymentApplication} entities in the database.
 * The main input is a {@link PaymentApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentApplication} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentApplicationQueryService extends QueryService<PaymentApplication> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentApplicationQueryService.class);

    private final PaymentApplicationRepository paymentApplicationRepository;

    public PaymentApplicationQueryService(PaymentApplicationRepository paymentApplicationRepository) {
        this.paymentApplicationRepository = paymentApplicationRepository;
    }

    /**
     * Return a {@link Page} of {@link PaymentApplication} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentApplication> findByCriteria(PaymentApplicationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentApplication> specification = createSpecification(criteria);
        return paymentApplicationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentApplicationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentApplication> specification = createSpecification(criteria);
        return paymentApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentApplication> createSpecification(PaymentApplicationCriteria criteria) {
        Specification<PaymentApplication> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PaymentApplication_.payment, JoinType.LEFT);
                root.fetch(PaymentApplication_.toPayment, JoinType.LEFT);
                root.fetch(PaymentApplication_.invoice, JoinType.LEFT);
                root.fetch(PaymentApplication_.invoiceItem, JoinType.LEFT);
                root.fetch(PaymentApplication_.order, JoinType.LEFT);
                root.fetch(PaymentApplication_.orderItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PaymentApplication_.id),
                    buildRangeSpecification(criteria.getAmountApplied(), PaymentApplication_.amountApplied),
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(PaymentApplication_.payment, JoinType.LEFT).get(Payment_.id)
                    ),
                    buildSpecification(criteria.getToPaymentId(), root ->
                        root.join(PaymentApplication_.toPayment, JoinType.LEFT).get(Payment_.id)
                    ),
                    buildSpecification(criteria.getInvoiceId(), root ->
                        root.join(PaymentApplication_.invoice, JoinType.LEFT).get(Invoice_.id)
                    ),
                    buildSpecification(criteria.getInvoiceItemId(), root ->
                        root.join(PaymentApplication_.invoiceItem, JoinType.LEFT).get(InvoiceItem_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root -> root.join(PaymentApplication_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(PaymentApplication_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
