package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InventoryItemVariance;
import com.snp.repository.InventoryItemVarianceRepository;
import com.snp.service.criteria.InventoryItemVarianceCriteria;
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
 * Service for executing complex queries for {@link InventoryItemVariance} entities in the database.
 * The main input is a {@link InventoryItemVarianceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryItemVariance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemVarianceQueryService extends QueryService<InventoryItemVariance> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemVarianceQueryService.class);

    private final InventoryItemVarianceRepository inventoryItemVarianceRepository;

    public InventoryItemVarianceQueryService(InventoryItemVarianceRepository inventoryItemVarianceRepository) {
        this.inventoryItemVarianceRepository = inventoryItemVarianceRepository;
    }

    /**
     * Return a {@link Page} of {@link InventoryItemVariance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItemVariance> findByCriteria(InventoryItemVarianceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryItemVariance> specification = createSpecification(criteria);
        return inventoryItemVarianceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemVarianceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItemVariance> specification = createSpecification(criteria);
        return inventoryItemVarianceRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemVarianceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItemVariance> createSpecification(InventoryItemVarianceCriteria criteria) {
        Specification<InventoryItemVariance> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(InventoryItemVariance_.inventoryItem, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InventoryItemVariance_.id),
                    buildStringSpecification(criteria.getVarianceReason(), InventoryItemVariance_.varianceReason),
                    buildRangeSpecification(criteria.getAtpVar(), InventoryItemVariance_.atpVar),
                    buildRangeSpecification(criteria.getQohVar(), InventoryItemVariance_.qohVar),
                    buildStringSpecification(criteria.getComments(), InventoryItemVariance_.comments),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(InventoryItemVariance_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    )
                )
            );
        }
        return specification;
    }
}
