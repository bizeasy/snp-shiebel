package com.snp.service;

import com.snp.domain.*; // for static metamodels
import com.snp.domain.Department;
import com.snp.repository.DepartmentRepository;
import com.snp.service.criteria.DepartmentCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Department} entities in the database.
 * The main input is a {@link DepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Department} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService extends QueryService<Department> {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentQueryService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Return a {@link List} of {@link Department} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Department> findByCriteria(DepartmentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Department> createSpecification(DepartmentCriteria criteria) {
        Specification<Department> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Department_.parent, JoinType.LEFT);
                root.fetch(Department_.head, JoinType.LEFT);
                root.fetch(Department_.productStore, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Department_.id),
                    buildStringSpecification(criteria.getName(), Department_.name),
                    buildStringSpecification(criteria.getCode(), Department_.code),
                    buildStringSpecification(criteria.getDescription(), Department_.description),
                    buildSpecification(criteria.getIsActive(), Department_.isActive),
                    buildSpecification(criteria.getParentId(), root -> root.join(Department_.parent, JoinType.LEFT).get(Department_.id)),
                    buildSpecification(criteria.getHeadId(), root -> root.join(Department_.head, JoinType.LEFT).get(Party_.id)),
                    buildSpecification(criteria.getProductStoreId(), root ->
                        root.join(Department_.productStore, JoinType.LEFT).get(ProductStore_.id)
                    )
                )
            );
        }
        return specification;
    }
}
