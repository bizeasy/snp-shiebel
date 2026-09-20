package com.snp.service;

import com.snp.domain.ContactMechType;
import com.snp.repository.ContactMechTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ContactMechType}.
 */
@Service
@Transactional
public class ContactMechTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechTypeService.class);

    private final ContactMechTypeRepository contactMechTypeRepository;

    public ContactMechTypeService(ContactMechTypeRepository contactMechTypeRepository) {
        this.contactMechTypeRepository = contactMechTypeRepository;
    }

    /**
     * Save a contactMechType.
     *
     * @param contactMechType the entity to save.
     * @return the persisted entity.
     */
    public ContactMechType save(ContactMechType contactMechType) {
        LOG.debug("Request to save ContactMechType : {}", contactMechType);
        return contactMechTypeRepository.save(contactMechType);
    }

    /**
     * Update a contactMechType.
     *
     * @param contactMechType the entity to save.
     * @return the persisted entity.
     */
    public ContactMechType update(ContactMechType contactMechType) {
        LOG.debug("Request to update ContactMechType : {}", contactMechType);
        return contactMechTypeRepository.save(contactMechType);
    }

    /**
     * Partially update a contactMechType.
     *
     * @param contactMechType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactMechType> partialUpdate(ContactMechType contactMechType) {
        LOG.debug("Request to partially update ContactMechType : {}", contactMechType);

        return contactMechTypeRepository
            .findById(contactMechType.getId())
            .map(existingContactMechType -> {
                updateIfPresent(existingContactMechType::setName, contactMechType.getName());
                updateIfPresent(existingContactMechType::setDescription, contactMechType.getDescription());

                return existingContactMechType;
            })
            .map(contactMechTypeRepository::save);
    }

    /**
     * Get one contactMechType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactMechType> findOne(Long id) {
        LOG.debug("Request to get ContactMechType : {}", id);
        return contactMechTypeRepository.findById(id);
    }

    /**
     * Delete the contactMechType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactMechType : {}", id);
        contactMechTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
