package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PickListItem;
import com.snp.repository.PickListItemRepository;
import com.snp.service.criteria.PickListItemCriteria;
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
 * Service for executing complex queries for {@link PickListItem} entities in the database.
 * The main input is a {@link PickListItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PickListItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PickListItemQueryService extends QueryService<PickListItem> {

    private static final Logger LOG = LoggerFactory.getLogger(PickListItemQueryService.class);

    private final PickListItemRepository pickListItemRepository;

    public PickListItemQueryService(PickListItemRepository pickListItemRepository) {
        this.pickListItemRepository = pickListItemRepository;
    }

    /**
     * Return a {@link Page} of {@link PickListItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PickListItem> findByCriteria(PickListItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PickListItem> specification = createSpecification(criteria);
        return pickListItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PickListItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PickListItem> specification = createSpecification(criteria);
        return pickListItemRepository.count(specification);
    }

    /**
     * Function to convert {@link PickListItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PickListItem> createSpecification(PickListItemCriteria criteria) {
        Specification<PickListItem> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PickListItem_.pickList, JoinType.LEFT);
                root.fetch(PickListItem_.order, JoinType.LEFT);
                root.fetch(PickListItem_.orderItem, JoinType.LEFT);
                root.fetch(PickListItem_.inventoryItem, JoinType.LEFT);
                root.fetch(PickListItem_.product, JoinType.LEFT);
                root.fetch(PickListItem_.facilityLocation, JoinType.LEFT);
                root.fetch(PickListItem_.status, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PickListItem_.id),
                    buildRangeSpecification(criteria.getQuantity(), PickListItem_.quantity),
                    buildSpecification(criteria.getPickListId(), root ->
                        root.join(PickListItem_.pickList, JoinType.LEFT).get(PickList_.id)
                    ),
                    buildSpecification(criteria.getOrderId(), root -> root.join(PickListItem_.order, JoinType.LEFT).get(Order_.id)),
                    buildSpecification(criteria.getOrderItemId(), root ->
                        root.join(PickListItem_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                    ),
                    buildSpecification(criteria.getInventoryItemId(), root ->
                        root.join(PickListItem_.inventoryItem, JoinType.LEFT).get(InventoryItem_.id)
                    ),
                    buildSpecification(criteria.getProductId(), root -> root.join(PickListItem_.product, JoinType.LEFT).get(Product_.id)),
                    buildSpecification(criteria.getFacilityLocationId(), root ->
                        root.join(PickListItem_.facilityLocation, JoinType.LEFT).get(FacilityLocation_.id)
                    ),
                    buildSpecification(criteria.getStatusId(), root -> root.join(PickListItem_.status, JoinType.LEFT).get(Status_.id))
                )
            );
        }
        return specification;
    }
}
