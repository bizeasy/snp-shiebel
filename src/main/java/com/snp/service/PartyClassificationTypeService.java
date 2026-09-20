package com.snp.service;

import com.snp.domain.PartyClassificationType;
import com.snp.repository.PartyClassificationTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PartyClassificationType}.
 */
@Service
@Transactional
public class PartyClassificationTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PartyClassificationTypeService.class);

    private final PartyClassificationTypeRepository partyClassificationTypeRepository;

    public PartyClassificationTypeService(PartyClassificationTypeRepository partyClassificationTypeRepository) {
        this.partyClassificationTypeRepository = partyClassificationTypeRepository;
    }

    /**
     * Save a partyClassificationType.
     *
     * @param partyClassificationType the entity to save.
     * @return the persisted entity.
     */
    public PartyClassificationType save(PartyClassificationType partyClassificationType) {
        LOG.debug("Request to save PartyClassificationType : {}", partyClassificationType);
        return partyClassificationTypeRepository.save(partyClassificationType);
    }

    /**
     * Update a partyClassificationType.
     *
     * @param partyClassificationType the entity to save.
     * @return the persisted entity.
     */
    public PartyClassificationType update(PartyClassificationType partyClassificationType) {
        LOG.debug("Request to update PartyClassificationType : {}", partyClassificationType);
        return partyClassificationTypeRepository.save(partyClassificationType);
    }

    /**
     * Partially update a partyClassificationType.
     *
     * @param partyClassificationType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartyClassificationType> partialUpdate(PartyClassificationType partyClassificationType) {
        LOG.debug("Request to partially update PartyClassificationType : {}", partyClassificationType);

        return partyClassificationTypeRepository
            .findById(partyClassificationType.getId())
            .map(existingPartyClassificationType -> {
                updateIfPresent(existingPartyClassificationType::setName, partyClassificationType.getName());
                updateIfPresent(existingPartyClassificationType::setDescription, partyClassificationType.getDescription());

                return existingPartyClassificationType;
            })
            .map(partyClassificationTypeRepository::save);
    }

    /**
     * Get one partyClassificationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartyClassificationType> findOne(Long id) {
        LOG.debug("Request to get PartyClassificationType : {}", id);
        return partyClassificationTypeRepository.findById(id);
    }

    /**
     * Delete the partyClassificationType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartyClassificationType : {}", id);
        partyClassificationTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
