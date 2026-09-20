package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Lot;
import com.snp.repository.LotRepository;
import com.snp.service.criteria.LotCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Lot} entities in the database.
 * The main input is a {@link LotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lot} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LotQueryService extends QueryService<Lot> {

    private static final Logger LOG = LoggerFactory.getLogger(LotQueryService.class);

    private final LotRepository lotRepository;

    public LotQueryService(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    /**
     * Return a {@link List} of {@link Lot} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lot> findByCriteria(LotCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Lot> specification = createSpecification(criteria);
        return lotRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LotCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Lot> specification = createSpecification(criteria);
        return lotRepository.count(specification);
    }

    /**
     * Function to convert {@link LotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lot> createSpecification(LotCriteria criteria) {
        Specification<Lot> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Lot_.id),
                    buildRangeSpecification(criteria.getCreationDate(), Lot_.creationDate),
                    buildRangeSpecification(criteria.getQuantity(), Lot_.quantity),
                    buildRangeSpecification(criteria.getExpirationDate(), Lot_.expirationDate),
                    buildRangeSpecification(criteria.getRetestDate(), Lot_.retestDate)
                )
            );
        }
        return specification;
    }
}
