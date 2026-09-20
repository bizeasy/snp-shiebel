package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Invoice;
import com.snp.repository.InvoiceRepository;
import com.snp.service.criteria.InvoiceCriteria;
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
 * Service for executing complex queries for {@link Invoice} entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Invoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Return a {@link Page} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Invoice_.invoiceType, JoinType.LEFT);
                root.fetch(Invoice_.order, JoinType.LEFT);
                root.fetch(Invoice_.fromParty, JoinType.LEFT);
                root.fetch(Invoice_.toParty, JoinType.LEFT);
                root.fetch(Invoice_.roleType, JoinType.LEFT);
                root.fetch(Invoice_.status, JoinType.LEFT);
                root.fetch(Invoice_.contactMech, JoinType.LEFT);
                root.fetch(Invoice_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Invoice_.id),
                    buildRangeSpecification(criteria.getInvoiceDate(), Invoice_.invoiceDate),
                    buildRangeSpecification(criteria.getDueDate(), Invoice_.dueDate),
                    buildRangeSpecification(criteria.getPaidDate(), Invoice_.paidDate),
                    buildStringSpecification(criteria.getReferenceNumber(), Invoice_.referenceNumber),
                    buildStringSpecification(criteria.getInvoiceMessage(), Invoice_.invoiceMessage),
                    buildStringSpecification(criteria.getExternalId(), Invoice_.externalId),
                    buildRangeSpecification(criteria.getEntryDate(), Invoice_.entryDate),
                    buildRangeSpecification(criteria.getGrandTotal(), Invoice_.grandTotal),
                    buildStringSpecification(criteria.getProjectName(), Invoice_.projectName),
                    buildStringSpecification(criteria.getRemarks(), Invoice_.remarks),
                    buildSpecification(criteria.getInvoiceTypeId(), root ->
                        root.join(Invoice_.invoiceType, JoinType.LEFT).get(InvoiceType_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root -> root.join(Invoice_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getFromPartyId(), root -> root.join(Invoice_.fromParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getToPartyId(), root -> root.join(Invoice_.toParty, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getRoleTypeId(), root -> root.join(Invoice_.roleType, JoinType.LEFT).get(RoleType_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(Invoice_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getContactMechId(), root ->
                        root.join(Invoice_.contactMech, JoinType.LEFT).get(ContactMech_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root -> root.join(Invoice_.currencyUom, JoinType.LEFT).get(Uom_.id))
                )
            );
        }
        return specification;
    }
}
