package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.InventoryItemType;
import com.snp.repository.InventoryItemTypeRepository;
import com.snp.service.criteria.InventoryItemTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InventoryItemType} entities in the database.
 * The main input is a {@link InventoryItemTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InventoryItemType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemTypeQueryService extends QueryService<InventoryItemType> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemTypeQueryService.class);

    private final InventoryItemTypeRepository inventoryItemTypeRepository;

    public InventoryItemTypeQueryService(InventoryItemTypeRepository inventoryItemTypeRepository) {
        this.inventoryItemTypeRepository = inventoryItemTypeRepository;
    }

    /**
     * Return a {@link List} of {@link InventoryItemType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InventoryItemType> findByCriteria(InventoryItemTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<InventoryItemType> specification = createSpecification(criteria);
        return inventoryItemTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItemType> specification = createSpecification(criteria);
        return inventoryItemTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItemType> createSpecification(InventoryItemTypeCriteria criteria) {
        Specification<InventoryItemType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), InventoryItemType_.id),
                    buildStringSpecification(criteria.getName(), InventoryItemType_.name),
                    buildStringSpecification(criteria.getDescription(), InventoryItemType_.description)
                )
            );
        }
        return specification;
    }
}
