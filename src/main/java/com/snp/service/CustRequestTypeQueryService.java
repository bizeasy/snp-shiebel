package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.CustRequestType;
import com.snp.repository.CustRequestTypeRepository;
import com.snp.service.criteria.CustRequestTypeCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustRequestType} entities in the database.
 * The main input is a {@link CustRequestTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustRequestType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustRequestTypeQueryService extends QueryService<CustRequestType> {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestTypeQueryService.class);

    private final CustRequestTypeRepository custRequestTypeRepository;

    public CustRequestTypeQueryService(CustRequestTypeRepository custRequestTypeRepository) {
        this.custRequestTypeRepository = custRequestTypeRepository;
    }

    /**
     * Return a {@link List} of {@link CustRequestType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustRequestType> findByCriteria(CustRequestTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CustRequestType> specification = createSpecification(criteria);
        return custRequestTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustRequestTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustRequestType> specification = createSpecification(criteria);
        return custRequestTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CustRequestTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustRequestType> createSpecification(CustRequestTypeCriteria criteria) {
        Specification<CustRequestType> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CustRequestType_.parent, JoinType.LEFT);
                root.fetch(CustRequestType_.party, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CustRequestType_.id),
                    buildStringSpecification(criteria.getName(), CustRequestType_.name),
                    buildStringSpecification(criteria.getDescription(), CustRequestType_.description),
                    buildSpecification(criteria.getParentId(), root ->
                        root.join(CustRequestType_.parent, JoinType.LEFT).get(CustRequestType_.id)
                    ),
                    buildSpecification(criteria.getPartyId(), root -> root.join(CustRequestType_.party, JoinType.LEFT).get(Party_.id))
                )
            );
        }
        return specification;
    }
}
