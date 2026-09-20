package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InvoiceType;
import com.snp.repository.InvoiceTypeRepository;
import com.snp.service.criteria.InvoiceTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InvoiceType} entities in the database.
 * The main input is a {@link InvoiceTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceTypeQueryService extends QueryService<InvoiceType> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceTypeQueryService.class);

    private final InvoiceTypeRepository invoiceTypeRepository;

    public InvoiceTypeQueryService(InvoiceTypeRepository invoiceTypeRepository) {
        this.invoiceTypeRepository = invoiceTypeRepository;
    }

    /**
     * Return a {@link List} of {@link InvoiceType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceType> findByCriteria(InvoiceTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<InvoiceType> specification = createSpecification(criteria);
        return invoiceTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceType> specification = createSpecification(criteria);
        return invoiceTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceType> createSpecification(InvoiceTypeCriteria criteria) {
        Specification<InvoiceType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InvoiceType_.id),
                    buildStringSpecification(criteria.getName(), InvoiceType_.name),
                    buildStringSpecification(criteria.getDescription(), InvoiceType_.description)
                )
            );
        }
        return specification;
    }
}
