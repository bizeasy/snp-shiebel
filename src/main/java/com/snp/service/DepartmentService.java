package com.snp.service;

import com.snp.domain.Department;
import com.snp.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Department}.
 */
@Service
@Transactional
public class DepartmentService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Save a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    public Department save(Department department) {
        LOG.debug("Request to save Department : {}", department);
        return departmentRepository.save(department);
    }

    /**
     * Update a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    public Department update(Department department) {
        LOG.debug("Request to update Department : {}", department);
        return departmentRepository.save(department);
    }

    /**
     * Partially update a department.
     *
     * @param department the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Department> partialUpdate(Department department) {
        LOG.debug("Request to partially update Department : {}", department);

        return departmentRepository
            .findById(department.getId())
            .map(existingDepartment -> {
                updateIfPresent(existingDepartment::setName, department.getName());
                updateIfPresent(existingDepartment::setCode, department.getCode());
                updateIfPresent(existingDepartment::setDescription, department.getDescription());
                updateIfPresent(existingDepartment::setIsActive, department.getIsActive());

                return existingDepartment;
            })
            .map(departmentRepository::save);
    }

    /**
     * Get all the departments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<Department> findAllWithEagerRelationships() {
        return departmentRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Department> findOne(Long id) {
        LOG.debug("Request to get Department : {}", id);
        return departmentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the department by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Department : {}", id);
        departmentRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
