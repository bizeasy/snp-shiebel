package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InvoiceItemTypeMap;
import com.snp.repository.InvoiceItemTypeMapRepository;
import com.snp.service.criteria.InvoiceItemTypeMapCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InvoiceItemTypeMap} entities in the database.
 * The main input is a {@link InvoiceItemTypeMapCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceItemTypeMap} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceItemTypeMapQueryService extends QueryService<InvoiceItemTypeMap> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeMapQueryService.class);

    private final InvoiceItemTypeMapRepository invoiceItemTypeMapRepository;

    public InvoiceItemTypeMapQueryService(InvoiceItemTypeMapRepository invoiceItemTypeMapRepository) {
        this.invoiceItemTypeMapRepository = invoiceItemTypeMapRepository;
    }

    /**
     * Return a {@link List} of {@link InvoiceItemTypeMap} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceItemTypeMap> findByCriteria(InvoiceItemTypeMapCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<InvoiceItemTypeMap> specification = createSpecification(criteria);
        return invoiceItemTypeMapRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceItemTypeMapCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceItemTypeMap> specification = createSpecification(criteria);
        return invoiceItemTypeMapRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceItemTypeMapCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceItemTypeMap> createSpecification(InvoiceItemTypeMapCriteria criteria) {
        Specification<InvoiceItemTypeMap> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InvoiceItemTypeMap_.invoiceType, JoinType.LEFT);
                root.fetch(InvoiceItemTypeMap_.invoiceItemType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InvoiceItemTypeMap_.id),
                    buildStringSpecification(criteria.getInvoiceItemMapKey(), InvoiceItemTypeMap_.invoiceItemMapKey),
                    buildSpecification(criteria.getInvoiceTypeId(), root ->
                        root.join(InvoiceItemTypeMap_.invoiceType, JoinType.LEFT).get(InvoiceType_.id)
                    ),
                    buildSpecification(criteria.getInvoiceItemTypeId(), root ->
                        root.join(InvoiceItemTypeMap_.invoiceItemType, JoinType.LEFT).get(InvoiceItemType_.id)
                    )
                )
            );
        }
        return specification;
    }
}
