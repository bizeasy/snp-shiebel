package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InvoiceItem;
import com.snp.repository.InvoiceItemRepository;
import com.snp.service.criteria.InvoiceItemCriteria;
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
 * Service for executing complex queries for {@link InvoiceItem} entities in the database.
 * The main input is a {@link InvoiceItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceItemQueryService extends QueryService<InvoiceItem> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemQueryService.class);

    private final InvoiceItemRepository invoiceItemRepository;

    public InvoiceItemQueryService(InvoiceItemRepository invoiceItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceItem> findByCriteria(InvoiceItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceItem> specification = createSpecification(criteria);
        return invoiceItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceItem> specification = createSpecification(criteria);
        return invoiceItemRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceItem> createSpecification(InvoiceItemCriteria criteria) {
        Specification<InvoiceItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InvoiceItem_.invoice, JoinType.LEFT);
                root.fetch(InvoiceItem_.invoiceItemType, JoinType.LEFT);
                root.fetch(InvoiceItem_.product, JoinType.LEFT);
                root.fetch(InvoiceItem_.uom, JoinType.LEFT);
                root.fetch(InvoiceItem_.productFeature, JoinType.LEFT);
                root.fetch(InvoiceItem_.productFacility, JoinType.LEFT);
                root.fetch(InvoiceItem_.fromInventoryItem, JoinType.LEFT);
                root.fetch(InvoiceItem_.taxAuthRate, JoinType.LEFT);
                root.fetch(InvoiceItem_.parentInvoice, JoinType.LEFT);
                root.fetch(InvoiceItem_.parentInvoiceItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InvoiceItem_.id),
                    buildRangeSpecification(criteria.getSequenceNo(), InvoiceItem_.sequenceNo),
                    buildRangeSpecification(criteria.getQuantity(), InvoiceItem_.quantity),
                    buildRangeSpecification(criteria.getAmount(), InvoiceItem_.amount),
                    buildRangeSpecification(criteria.getCancelQuantity(), InvoiceItem_.cancelQuantity),
                    buildRangeSpecification(criteria.getSelectedAmount(), InvoiceItem_.selectedAmount),
                    buildRangeSpecification(criteria.getUnitPrice(), InvoiceItem_.unitPrice),
                    buildRangeSpecification(criteria.getUnitListPrice(), InvoiceItem_.unitListPrice),
                    buildSpecification(criteria.getIsModifiedPrice(), InvoiceItem_.isModifiedPrice),
                    buildRangeSpecification(criteria.getEstimatedShipDate(), InvoiceItem_.estimatedShipDate),
                    buildRangeSpecification(criteria.getEstimatedDeliveryDate(), InvoiceItem_.estimatedDeliveryDate),
                    buildRangeSpecification(criteria.getShipBeforeDate(), InvoiceItem_.shipBeforeDate),
                    buildRangeSpecification(criteria.getShipAfterDate(), InvoiceItem_.shipAfterDate),
                    buildRangeSpecification(criteria.getEntryDate(), InvoiceItem_.entryDate),
                    buildStringSpecification(criteria.getAttr1(), InvoiceItem_.attr1),
                    buildStringSpecification(criteria.getAttr2(), InvoiceItem_.attr2),
                    buildStringSpecification(criteria.getAttr3(), InvoiceItem_.attr3),
                    buildSpecification(criteria.getInvoiceId(), root -> root.join(InvoiceItem_.invoice, JoinType.LEFT).get(Invoice_.id)),
                    buildSpecification(criteria.getInvoiceItemTypeId(), root ->
                        root.join(InvoiceItem_.invoiceItemType, JoinType.LEFT).get(InvoiceItemType_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(InvoiceItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getUomId(), root -> root.join(InvoiceItem_.uom, JoinType.LEFT).get(Uom_.id)),
                    buildSpecification(criteria.getProductFeatureId(), root ->
                        root.join(InvoiceItem_.productFeature, JoinType.LEFT).get(ProductFeature_.id)
                    ),
                    buildSpecification(criteria.getProductFacilityId(), root ->
                        root.join(InvoiceItem_.productFacility, JoinType.LEFT).get(ProductFacility_.id)
                    ),
                    buildSpecification(criteria.getFromInventoryItemId(), root ->
                        root.join(InvoiceItem_.fromInventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getTaxAuthRateId(), root ->
                        root.join(InvoiceItem_.taxAuthRate, JoinType.LEFT).get(TaxAuthorityRateType_.id)
                    ),
                    buildSpecification(criteria.getParentInvoiceId(), root ->
                        root.join(InvoiceItem_.parentInvoice, JoinType.LEFT).get(Invoice_.id)
                    ),
                    buildSpecification(criteria.getParentInvoiceItemId(), root ->
                        root.join(InvoiceItem_.parentInvoiceItem, JoinType.LEFT).get(InvoiceItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
