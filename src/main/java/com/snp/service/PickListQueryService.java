package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.PickList;
import com.snp.repository.PickListRepository;
import com.snp.service.criteria.PickListCriteria;
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
 * Service for executing complex queries for {@link PickList} entities in the database.
 * The main input is a {@link PickListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PickList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PickListQueryService extends QueryService<PickList> {

    private static final Logger LOG = LoggerFactory.getLogger(PickListQueryService.class);

    private final PickListRepository pickListRepository;

    public PickListQueryService(PickListRepository pickListRepository) {
        this.pickListRepository = pickListRepository;
    }

    /**
     * Return a {@link Page} of {@link PickList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PickList> findByCriteria(PickListCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PickList> specification = createSpecification(criteria);
        return pickListRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PickListCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PickList> specification = createSpecification(criteria);
        return pickListRepository.count(specification);
    }

    /**
     * Function to convert {@link PickListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PickList> createSpecification(PickListCriteria criteria) {
        Specification<PickList> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(PickList_.facility, JoinType.LEFT);
                root.fetch(PickList_.status, JoinType.LEFT);
                root.fetch(PickList_.party, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), PickList_.id),
                    buildStringSpecification(criteria.getDescription(), PickList_.description),
                    buildRangeSpecification(criteria.getPickListDate(), PickList_.pickListDate),
                    buildSpecification(criteria.getFacilityId(), root -> root.join(PickList_.facility, JoinType.LEFT).get(Facility_.id)),
                    buildSpecification(criteria.getStatusId(), root -> root.join(PickList_.status, JoinType.LEFT).get(Status_.id)),
                    buildSpecification(criteria.getPartyId(), root -> root.join(PickList_.party, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
