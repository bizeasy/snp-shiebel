package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InvoiceItemType;
import com.snp.repository.InvoiceItemTypeRepository;
import com.snp.service.criteria.InvoiceItemTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InvoiceItemType} entities in the database.
 * The main input is a {@link InvoiceItemTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceItemType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceItemTypeQueryService extends QueryService<InvoiceItemType> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeQueryService.class);

    private final InvoiceItemTypeRepository invoiceItemTypeRepository;

    public InvoiceItemTypeQueryService(InvoiceItemTypeRepository invoiceItemTypeRepository) {
        this.invoiceItemTypeRepository = invoiceItemTypeRepository;
    }

    /**
     * Return a {@link List} of {@link InvoiceItemType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceItemType> findByCriteria(InvoiceItemTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<InvoiceItemType> specification = createSpecification(criteria);
        return invoiceItemTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceItemTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceItemType> specification = createSpecification(criteria);
        return invoiceItemTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceItemTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceItemType> createSpecification(InvoiceItemTypeCriteria criteria) {
        Specification<InvoiceItemType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InvoiceItemType_.id),
                    buildStringSpecification(criteria.getName(), InvoiceItemType_.name),
                    buildStringSpecification(criteria.getDescription(), InvoiceItemType_.description)
                )
            );
        }
        return specification;
    }
}
