package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.TransactionType;
import com.snp.repository.TransactionTypeRepository;
import com.snp.service.criteria.TransactionTypeCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransactionType} entities in the database.
 * The main input is a {@link TransactionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionTypeQueryService extends QueryService<TransactionType> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeQueryService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeQueryService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionType> findByCriteria(TransactionTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionType> createSpecification(TransactionTypeCriteria criteria) {
        Specification<TransactionType> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TransactionType_.category, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TransactionType_.id),
                    buildStringSpecification(criteria.getName(), TransactionType_.name),
                    buildStringSpecification(criteria.getDescription(), TransactionType_.description),
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(TransactionType_.category, JoinType.LEFT).get(TransactionCategory_.id)
                    )
                )
            );
        }
        return specification;
    }
}
