package com.snp.service;

import com.snp.domain.DepartmentMember;
import com.snp.repository.DepartmentMemberRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.DepartmentMember}.
 */
@Service
@Transactional
public class DepartmentMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentMemberService.class);

    private final DepartmentMemberRepository departmentMemberRepository;

    public DepartmentMemberService(DepartmentMemberRepository departmentMemberRepository) {
        this.departmentMemberRepository = departmentMemberRepository;
    }

    /**
     * Save a departmentMember.
     *
     * @param departmentMember the entity to save.
     * @return the persisted entity.
     */
    public DepartmentMember save(DepartmentMember departmentMember) {
        LOG.debug("Request to save DepartmentMember : {}", departmentMember);
        return departmentMemberRepository.save(departmentMember);
    }

    /**
     * Update a departmentMember.
     *
     * @param departmentMember the entity to save.
     * @return the persisted entity.
     */
    public DepartmentMember update(DepartmentMember departmentMember) {
        LOG.debug("Request to update DepartmentMember : {}", departmentMember);
        return departmentMemberRepository.save(departmentMember);
    }

    /**
     * Partially update a departmentMember.
     *
     * @param departmentMember the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartmentMember> partialUpdate(DepartmentMember departmentMember) {
        LOG.debug("Request to partially update DepartmentMember : {}", departmentMember);

        return departmentMemberRepository
            .findById(departmentMember.getId())
            .map(existingDepartmentMember -> {
                updateIfPresent(existingDepartmentMember::setFromDate, departmentMember.getFromDate());
                updateIfPresent(existingDepartmentMember::setThruDate, departmentMember.getThruDate());
                updateIfPresent(existingDepartmentMember::setIsPrimary, departmentMember.getIsPrimary());

                return existingDepartmentMember;
            })
            .map(departmentMemberRepository::save);
    }

    /**
     * Get all the departmentMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DepartmentMember> findAllWithEagerRelationships(Pageable pageable) {
        return departmentMemberRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one departmentMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentMember> findOne(Long id) {
        LOG.debug("Request to get DepartmentMember : {}", id);
        return departmentMemberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the departmentMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DepartmentMember : {}", id);
        departmentMemberRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
