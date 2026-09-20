package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CashBook;
import com.snp.repository.CashBookRepository;
import com.snp.service.criteria.CashBookCriteria;
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
 * Service for executing complex queries for {@link CashBook} entities in the database.
 * The main input is a {@link CashBookCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CashBook} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CashBookQueryService extends QueryService<CashBook> {

    private static final Logger LOG = LoggerFactory.getLogger(CashBookQueryService.class);

    private final CashBookRepository cashBookRepository;

    public CashBookQueryService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    /**
     * Return a {@link Page} of {@link CashBook} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBook> findByCriteria(CashBookCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CashBook> specification = createSpecification(criteria);
        return cashBookRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CashBookCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CashBook> specification = createSpecification(criteria);
        return cashBookRepository.count(specification);
    }

    /**
     * Function to convert {@link CashBookCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CashBook> createSpecification(CashBookCriteria criteria) {
        Specification<CashBook> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CashBook_.transactionType, JoinType.LEFT);
                root.fetch(CashBook_.transactionCategory, JoinType.LEFT);
                root.fetch(CashBook_.paymentMethodType, JoinType.LEFT);
                root.fetch(CashBook_.department, JoinType.LEFT);
                root.fetch(CashBook_.currencyUom, JoinType.LEFT);
                root.fetch(CashBook_.status, JoinType.LEFT);
                root.fetch(CashBook_.order, JoinType.LEFT);
                root.fetch(CashBook_.invoice, JoinType.LEFT);
                root.fetch(CashBook_.fromParty, JoinType.LEFT);
                root.fetch(CashBook_.toParty, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CashBook_.id),
                    buildRangeSpecification(criteria.getTransactionDate(), CashBook_.transactionDate),
                    buildRangeSpecification(criteria.getDueDate(), CashBook_.dueDate),
                    buildRangeSpecification(criteria.getAmount(), CashBook_.amount),
                    buildStringSpecification(criteria.getDescription(), CashBook_.description),
                    buildStringSpecification(criteria.getVendorCustomerName(), CashBook_.vendorCustomerName),
                    buildStringSpecification(criteria.getInvoiceNo(), CashBook_.invoiceNo),
                    buildRangeSpecification(criteria.getInvoiceDate(), CashBook_.invoiceDate),
                    buildStringSpecification(criteria.getPurpose(), CashBook_.purpose),
                    buildStringSpecification(criteria.getRemarks(), CashBook_.remarks),
                    buildStringSpecification(criteria.getProject(), CashBook_.project),
                    buildStringSpecification(criteria.getUsageUnits(), CashBook_.usageUnits),
                    buildSpecification(criteria.getRecurringTransaction(), CashBook_.recurringTransaction),
                    buildSpecification(criteria.getFrequency(), CashBook_.frequency),
                    buildSpecification(criteria.getTransactionTypeId(), root ->
                        root.join(CashBook_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                    ),
                    buildSpecification(criteria.getTransactionCategoryId(), root ->
                        root.join(CashBook_.transactionCategory, JoinType.LEFT).get(TransactionCategory_.id)
                    ),
                    buildSpecification(criteria.getPaymentMethodTypeId(), root ->
                        root.join(CashBook_.paymentMethodType, JoinType.LEFT).get(PaymentMethodType_.id)
                    ),
                    buildSpecification(criteria.getDepartmentId(), root ->
                        root.join(CashBook_.department, JoinType.LEFT).get(Department_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(CashBook_.currencyUom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(CashBook_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getOrderId(), root -> root.join(CashBook_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getInvoiceId(), root -> root.join(CashBook_.invoice, JoinType.LEFT).get(Invoice_.id)),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(CashBook_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getToPartyId(), root -> root.join(CashBook_.toParty, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
