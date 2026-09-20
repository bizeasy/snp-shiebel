package com.snp.service;

import com.snp.domain.RoleType;
import com.snp.repository.RoleTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.RoleType}.
 */
@Service
@Transactional
public class RoleTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeService.class);

    private final RoleTypeRepository roleTypeRepository;

    public RoleTypeService(RoleTypeRepository roleTypeRepository) {
        this.roleTypeRepository = roleTypeRepository;
    }

    /**
     * Save a roleType.
     *
     * @param roleType the entity to save.
     * @return the persisted entity.
     */
    public RoleType save(RoleType roleType) {
        LOG.debug("Request to save RoleType : {}", roleType);
        return roleTypeRepository.save(roleType);
    }

    /**
     * Update a roleType.
     *
     * @param roleType the entity to save.
     * @return the persisted entity.
     */
    public RoleType update(RoleType roleType) {
        LOG.debug("Request to update RoleType : {}", roleType);
        return roleTypeRepository.save(roleType);
    }

    /**
     * Partially update a roleType.
     *
     * @param roleType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoleType> partialUpdate(RoleType roleType) {
        LOG.debug("Request to partially update RoleType : {}", roleType);

        return roleTypeRepository
            .findById(roleType.getId())
            .map(existingRoleType -> {
                updateIfPresent(existingRoleType::setName, roleType.getName());
                updateIfPresent(existingRoleType::setDescription, roleType.getDescription());

                return existingRoleType;
            })
            .map(roleTypeRepository::save);
    }

    /**
     * Get one roleType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleType> findOne(Long id) {
        LOG.debug("Request to get RoleType : {}", id);
        return roleTypeRepository.findById(id);
    }

    /**
     * Delete the roleType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RoleType : {}", id);
        roleTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
