package com.snp.service;

import com.snp.domain.PartyRelationshipType;
import com.snp.repository.PartyRelationshipTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyRelationshipType}.
 */
@Service
@Transactional
public class PartyRelationshipTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyRelationshipTypeService.class);

    private final PartyRelationshipTypeRepository partyRelationshipTypeRepository;

    public PartyRelationshipTypeService(PartyRelationshipTypeRepository partyRelationshipTypeRepository) {
        this.partyRelationshipTypeRepository = partyRelationshipTypeRepository;
    }

    /**
     * Save a partyRelationshipType.
     *
     * @param partyRelationshipType the entity to save.
     * @return the persisted entity.
     */
    public PartyRelationshipType save(PartyRelationshipType partyRelationshipType) {
        LOG.debug("Request to save PartyRelationshipType : {}", partyRelationshipType);
        return partyRelationshipTypeRepository.save(partyRelationshipType);
    }

    /**
     * Update a partyRelationshipType.
     *
     * @param partyRelationshipType the entity to save.
     * @return the persisted entity.
     */
    public PartyRelationshipType update(PartyRelationshipType partyRelationshipType) {
        LOG.debug("Request to update PartyRelationshipType : {}", partyRelationshipType);
        return partyRelationshipTypeRepository.save(partyRelationshipType);
    }

    /**
     * Partially update a partyRelationshipType.
     *
     * @param partyRelationshipType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyRelationshipType> partialUpdate(PartyRelationshipType partyRelationshipType) {
        LOG.debug("Request to partially update PartyRelationshipType : {}", partyRelationshipType);

        return partyRelationshipTypeRepository
            .findById(partyRelationshipType.getId())
            .map(existingPartyRelationshipType -> {
                updateIfPresent(existingPartyRelationshipType::setName, partyRelationshipType.getName());
                updateIfPresent(existingPartyRelationshipType::setDescription, partyRelationshipType.getDescription());

                return existingPartyRelationshipType;
            })
            .map(partyRelationshipTypeRepository::save);
    }

    /**
     * Get all the partyRelationshipTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<PartyRelationshipType> findAllWithEagerRelationships() {
        return partyRelationshipTypeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one partyRelationshipType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyRelationshipType> findOne(Long id) {
        LOG.debug("Request to get PartyRelationshipType : {}", id);
        return partyRelationshipTypeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partyRelationshipType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyRelationshipType : {}", id);
        partyRelationshipTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
