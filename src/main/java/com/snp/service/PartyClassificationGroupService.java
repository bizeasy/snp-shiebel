package com.snp.service;

import com.snp.domain.PartyClassificationGroup;
import com.snp.repository.PartyClassificationGroupRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyClassificationGroup}.
 */
@Service
@Transactional
public class PartyClassificationGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationGroupService.class);

    private final PartyClassificationGroupRepository partyClassificationGroupRepository;

    public PartyClassificationGroupService(PartyClassificationGroupRepository partyClassificationGroupRepository) {
        this.partyClassificationGroupRepository = partyClassificationGroupRepository;
    }

    /**
     * Save a partyClassificationGroup.
     *
     * @param partyClassificationGroup the entity to save.
     * @return the persisted entity.
     */
    public PartyClassificationGroup save(PartyClassificationGroup partyClassificationGroup) {
        LOG.debug("Request to save PartyClassificationGroup : {}", partyClassificationGroup);
        return partyClassificationGroupRepository.save(partyClassificationGroup);
    }

    /**
     * Update a partyClassificationGroup.
     *
     * @param partyClassificationGroup the entity to save.
     * @return the persisted entity.
     */
    public PartyClassificationGroup update(PartyClassificationGroup partyClassificationGroup) {
        LOG.debug("Request to update PartyClassificationGroup : {}", partyClassificationGroup);
        return partyClassificationGroupRepository.save(partyClassificationGroup);
    }

    /**
     * Partially update a partyClassificationGroup.
     *
     * @param partyClassificationGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyClassificationGroup> partialUpdate(PartyClassificationGroup partyClassificationGroup) {
        LOG.debug("Request to partially update PartyClassificationGroup : {}", partyClassificationGroup);

        return partyClassificationGroupRepository
            .findById(partyClassificationGroup.getId())
            .map(existingPartyClassificationGroup -> {
                updateIfPresent(existingPartyClassificationGroup::setName, partyClassificationGroup.getName());
                updateIfPresent(existingPartyClassificationGroup::setDescription, partyClassificationGroup.getDescription());

                return existingPartyClassificationGroup;
            })
            .map(partyClassificationGroupRepository::save);
    }

    /**
     * Get all the partyClassificationGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<PartyClassificationGroup> findAllWithEagerRelationships() {
        return partyClassificationGroupRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one partyClassificationGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyClassificationGroup> findOne(Long id) {
        LOG.debug("Request to get PartyClassificationGroup : {}", id);
        return partyClassificationGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyClassificationGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyClassificationGroup : {}", id);
        partyClassificationGroupRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
