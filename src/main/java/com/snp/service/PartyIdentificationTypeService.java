package com.snp.service;

import com.snp.domain.PartyIdentificationType;
import com.snp.repository.PartyIdentificationTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyIdentificationType}.
 */
@Service
@Transactional
public class PartyIdentificationTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyIdentificationTypeService.class);

    private final PartyIdentificationTypeRepository partyIdentificationTypeRepository;

    public PartyIdentificationTypeService(PartyIdentificationTypeRepository partyIdentificationTypeRepository) {
        this.partyIdentificationTypeRepository = partyIdentificationTypeRepository;
    }

    /**
     * Save a partyIdentificationType.
     *
     * @param partyIdentificationType the entity to save.
     * @return the persisted entity.
     */
    public PartyIdentificationType save(PartyIdentificationType partyIdentificationType) {
        LOG.debug("Request to save PartyIdentificationType : {}", partyIdentificationType);
        return partyIdentificationTypeRepository.save(partyIdentificationType);
    }

    /**
     * Update a partyIdentificationType.
     *
     * @param partyIdentificationType the entity to save.
     * @return the persisted entity.
     */
    public PartyIdentificationType update(PartyIdentificationType partyIdentificationType) {
        LOG.debug("Request to update PartyIdentificationType : {}", partyIdentificationType);
        return partyIdentificationTypeRepository.save(partyIdentificationType);
    }

    /**
     * Partially update a partyIdentificationType.
     *
     * @param partyIdentificationType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyIdentificationType> partialUpdate(PartyIdentificationType partyIdentificationType) {
        LOG.debug("Request to partially update PartyIdentificationType : {}", partyIdentificationType);

        return partyIdentificationTypeRepository
            .findById(partyIdentificationType.getId())
            .map(existingPartyIdentificationType -> {
                updateIfPresent(existingPartyIdentificationType::setName, partyIdentificationType.getName());
                updateIfPresent(existingPartyIdentificationType::setDescription, partyIdentificationType.getDescription());

                return existingPartyIdentificationType;
            })
            .map(partyIdentificationTypeRepository::save);
    }

    /**
     * Get one partyIdentificationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyIdentificationType> findOne(Long id) {
        LOG.debug("Request to get PartyIdentificationType : {}", id);
        return partyIdentificationTypeRepository.findById(id);
    }

    /**
     * Delete the partyIdentificationType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyIdentificationType : {}", id);
        partyIdentificationTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
