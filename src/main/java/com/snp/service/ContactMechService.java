package com.snp.service;

import com.snp.domain.ContactMech;
import com.snp.repository.ContactMechRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ContactMech}.
 */
@Service
@Transactional
public class ContactMechService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactMechService.class);

    private final ContactMechRepository contactMechRepository;

    public ContactMechService(ContactMechRepository contactMechRepository) {
        this.contactMechRepository = contactMechRepository;
    }

    /**
     * Save a contactMech.
     *
     * @param contactMech the entity to save.
     * @return the persisted entity.
     */
    public ContactMech save(ContactMech contactMech) {
        LOG.debug("Request to save ContactMech : {}", contactMech);
        return contactMechRepository.save(contactMech);
    }

    /**
     * Update a contactMech.
     *
     * @param contactMech the entity to save.
     * @return the persisted entity.
     */
    public ContactMech update(ContactMech contactMech) {
        LOG.debug("Request to update ContactMech : {}", contactMech);
        return contactMechRepository.save(contactMech);
    }

    /**
     * Partially update a contactMech.
     *
     * @param contactMech the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactMech> partialUpdate(ContactMech contactMech) {
        LOG.debug("Request to partially update ContactMech : {}", contactMech);

        return contactMechRepository
            .findById(contactMech.getId())
            .map(existingContactMech -> {
                updateIfPresent(existingContactMech::setInfoString, contactMech.getInfoString());

                return existingContactMech;
            })
            .map(contactMechRepository::save);
    }

    /**
     * Get all the contactMeches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContactMech> findAllWithEagerRelationships(Pageable pageable) {
        return contactMechRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one contactMech by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactMech> findOne(Long id) {
        LOG.debug("Request to get ContactMech : {}", id);
        return contactMechRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the contactMech by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactMech : {}", id);
        contactMechRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
