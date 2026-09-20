package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.SupplierProduct;
import com.snp.repository.SupplierProductRepository;
import com.snp.service.criteria.SupplierProductCriteria;
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
 * Service for executing complex queries for {@link SupplierProduct} entities in the database.
 * The main input is a {@link SupplierProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierProductQueryService extends QueryService<SupplierProduct> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierProductQueryService.class);

    private final SupplierProductRepository supplierProductRepository;

    public SupplierProductQueryService(SupplierProductRepository supplierProductRepository) {
        this.supplierProductRepository = supplierProductRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierProduct> findByCriteria(SupplierProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierProduct> specification = createSpecification(criteria);
        return supplierProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierProduct> specification = createSpecification(criteria);
        return supplierProductRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierProduct> createSpecification(SupplierProductCriteria criteria) {
        Specification<SupplierProduct> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(SupplierProduct_.product, JoinType.LEFT);
                root.fetch(SupplierProduct_.party, JoinType.LEFT);
                root.fetch(SupplierProduct_.quantityUom, JoinType.LEFT);
                root.fetch(SupplierProduct_.currencyUom, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SupplierProduct_.id),
                    buildRangeSpecification(criteria.getFromDate(), SupplierProduct_.fromDate),
                    buildRangeSpecification(criteria.getThruDate(), SupplierProduct_.thruDate),
                    buildStringSpecification(criteria.getSupplierProductId(), SupplierProduct_.supplierProductId),
                    buildRangeSpecification(criteria.getMinOrderQty(), SupplierProduct_.minOrderQty),
                    buildRangeSpecification(criteria.getLastPrice(), SupplierProduct_.lastPrice),
                    buildRangeSpecification(criteria.getShippingPrice(), SupplierProduct_.shippingPrice),
                    buildRangeSpecification(criteria.getCgst(), SupplierProduct_.cgst),
                    buildRangeSpecification(criteria.getIgst(), SupplierProduct_.igst),
                    buildRangeSpecification(criteria.getSgst(), SupplierProduct_.sgst),
                    buildRangeSpecification(criteria.getTotalPrice(), SupplierProduct_.totalPrice),
                    buildStringSpecification(criteria.getComments(), SupplierProduct_.comments),
                    buildRangeSpecification(criteria.getLeadTimeDays(), SupplierProduct_.leadTimeDays),
                    buildRangeSpecification(criteria.getUnitsIncluded(), SupplierProduct_.unitsIncluded),
                    buildSpecification(criteria.getCanDropShip(), SupplierProduct_.canDropShip),
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(SupplierProduct_.product, JoinType.LEFT).get(Product_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(SupplierProduct_.party, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getQuantityUomId(), root ->
                        root.join(SupplierProduct_.quantityUom, JoinType.LEFT).get(Uom_.id)
                    ),
                    buildSpecification(criteria.getCurrencyUomId(), root ->
                        root.join(SupplierProduct_.currencyUom, JoinType.LEFT).get(Uom_.id)
                    )
                )
            );
        }
        return specification;
    }
}
