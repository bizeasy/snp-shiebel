package com.snp.service;

import com.snp.domain.ContactMechPurpose;
import com.snp.repository.ContactMechPurposeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ContactMechPurpose}.
 */
@Service
@Transactional
public class ContactMechPurposeService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechPurposeService.class);

    private final ContactMechPurposeRepository contactMechPurposeRepository;

    public ContactMechPurposeService(ContactMechPurposeRepository contactMechPurposeRepository) {
        this.contactMechPurposeRepository = contactMechPurposeRepository;
    }

    /**
     * Save a contactMechPurpose.
     *
     * @param contactMechPurpose the entity to save.
     * @return the persisted entity.
     */
    public ContactMechPurpose save(ContactMechPurpose contactMechPurpose) {
        LOG.debug("Request to save ContactMechPurpose : {}", contactMechPurpose);
        return contactMechPurposeRepository.save(contactMechPurpose);
    }

    /**
     * Update a contactMechPurpose.
     *
     * @param contactMechPurpose the entity to save.
     * @return the persisted entity.
     */
    public ContactMechPurpose update(ContactMechPurpose contactMechPurpose) {
        LOG.debug("Request to update ContactMechPurpose : {}", contactMechPurpose);
        return contactMechPurposeRepository.save(contactMechPurpose);
    }

    /**
     * Partially update a contactMechPurpose.
     *
     * @param contactMechPurpose the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactMechPurpose> partialUpdate(ContactMechPurpose contactMechPurpose) {
        LOG.debug("Request to partially update ContactMechPurpose : {}", contactMechPurpose);

        return contactMechPurposeRepository
            .findById(contactMechPurpose.getId())
            .map(existingContactMechPurpose -> {
                updateIfPresent(existingContactMechPurpose::setName, contactMechPurpose.getName());
                updateIfPresent(existingContactMechPurpose::setDescription, contactMechPurpose.getDescription());

                return existingContactMechPurpose;
            })
            .map(contactMechPurposeRepository::save);
    }

    /**
     * Get one contactMechPurpose by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactMechPurpose> findOne(Long id) {
        LOG.debug("Request to get ContactMechPurpose : {}", id);
        return contactMechPurposeRepository.findById(id);
    }

    /**
     * Delete the contactMechPurpose by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactMechPurpose : {}", id);
        contactMechPurposeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
