package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.RoleType;
import com.snp.repository.RoleTypeRepository;
import com.snp.service.criteria.RoleTypeCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link RoleType} entities in the database.
 * The main input is a {@link RoleTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RoleType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoleTypeQueryService extends QueryService<RoleType> {

    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeQueryService.class);

    private final RoleTypeRepository roleTypeRepository;

    public RoleTypeQueryService(RoleTypeRepository roleTypeRepository) {
        this.roleTypeRepository = roleTypeRepository;
    }

    /**
     * Return a {@link List} of {@link RoleType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RoleType> findByCriteria(RoleTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<RoleType> specification = createSpecification(criteria);
        return roleTypeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoleTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RoleType> specification = createSpecification(criteria);
        return roleTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link RoleTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RoleType> createSpecification(RoleTypeCriteria criteria) {
        Specification<RoleType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), RoleType_.id),
                    buildStringSpecification(criteria.getName(), RoleType_.name),
                    buildStringSpecification(criteria.getDescription(), RoleType_.description)
                )
            );
        }
        return specification;
    }
}
