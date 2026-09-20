package com.snp.service;

import com.snp.domain.FacilityGroup;
import com.snp.repository.FacilityGroupRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.FacilityGroup}.
 */
@Service
@Transactional
public class FacilityGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityGroupService.class);

    private final FacilityGroupRepository facilityGroupRepository;

    public FacilityGroupService(FacilityGroupRepository facilityGroupRepository) {
        this.facilityGroupRepository = facilityGroupRepository;
    }

    /**
     * Save a facilityGroup.
     *
     * @param facilityGroup the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroup save(FacilityGroup facilityGroup) {
        LOG.debug("Request to save FacilityGroup : {}", facilityGroup);
        return facilityGroupRepository.save(facilityGroup);
    }

    /**
     * Update a facilityGroup.
     *
     * @param facilityGroup the entity to save.
     * @return the persisted entity.
     */
    public FacilityGroup update(FacilityGroup facilityGroup) {
        LOG.debug("Request to update FacilityGroup : {}", facilityGroup);
        return facilityGroupRepository.save(facilityGroup);
    }

    /**
     * Partially update a facilityGroup.
     *
     * @param facilityGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityGroup> partialUpdate(FacilityGroup facilityGroup) {
        LOG.debug("Request to partially update FacilityGroup : {}", facilityGroup);

        return facilityGroupRepository
            .findById(facilityGroup.getId())
            .map(existingFacilityGroup -> {
                updateIfPresent(existingFacilityGroup::setName, facilityGroup.getName());
                updateIfPresent(existingFacilityGroup::setDescription, facilityGroup.getDescription());

                return existingFacilityGroup;
            })
            .map(facilityGroupRepository::save);
    }

    /**
     * Get all the facilityGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<FacilityGroup> findAllWithEagerRelationships() {
        return facilityGroupRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one facilityGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityGroup> findOne(Long id) {
        LOG.debug("Request to get FacilityGroup : {}", id);
        return facilityGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the facilityGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityGroup : {}", id);
        facilityGroupRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
