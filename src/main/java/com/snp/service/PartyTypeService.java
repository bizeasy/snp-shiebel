package com.snp.service;

import com.snp.domain.PartyType;
import com.snp.repository.PartyTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyType}.
 */
@Service
@Transactional
public class PartyTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyTypeService.class);

    private final PartyTypeRepository partyTypeRepository;

    public PartyTypeService(PartyTypeRepository partyTypeRepository) {
        this.partyTypeRepository = partyTypeRepository;
    }

    /**
     * Save a partyType.
     *
     * @param partyType the entity to save.
     * @return the persisted entity.
     */
    public PartyType save(PartyType partyType) {
        LOG.debug("Request to save PartyType : {}", partyType);
        return partyTypeRepository.save(partyType);
    }

    /**
     * Update a partyType.
     *
     * @param partyType the entity to save.
     * @return the persisted entity.
     */
    public PartyType update(PartyType partyType) {
        LOG.debug("Request to update PartyType : {}", partyType);
        return partyTypeRepository.save(partyType);
    }

    /**
     * Partially update a partyType.
     *
     * @param partyType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyType> partialUpdate(PartyType partyType) {
        LOG.debug("Request to partially update PartyType : {}", partyType);

        return partyTypeRepository
            .findById(partyType.getId())
            .map(existingPartyType -> {
                updateIfPresent(existingPartyType::setName, partyType.getName());
                updateIfPresent(existingPartyType::setDescription, partyType.getDescription());

                return existingPartyType;
            })
            .map(partyTypeRepository::save);
    }

    /**
     * Get one partyType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyType> findOne(Long id) {
        LOG.debug("Request to get PartyType : {}", id);
        return partyTypeRepository.findById(id);
    }

    /**
     * Delete the partyType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyType : {}", id);
        partyTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
