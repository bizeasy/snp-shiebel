package com.snp.service;

import com.snp.domain.PostalAddress;
import com.snp.repository.PostalAddressRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.PostalAddress}.
 */
@Service
@Transactional
public class PostalAddressService {

    private static final Logger LOG = LoggerFactory.getLogger(PostalAddressService.class);

    private final PostalAddressRepository postalAddressRepository;

    public PostalAddressService(PostalAddressRepository postalAddressRepository) {
        this.postalAddressRepository = postalAddressRepository;
    }

    /**
     * Save a postalAddress.
     *
     * @param postalAddress the entity to save.
     * @return the persisted entity.
     */
    public PostalAddress save(PostalAddress postalAddress) {
        LOG.debug("Request to save PostalAddress : {}", postalAddress);
        return postalAddressRepository.save(postalAddress);
    }

    /**
     * Update a postalAddress.
     *
     * @param postalAddress the entity to save.
     * @return the persisted entity.
     */
    public PostalAddress update(PostalAddress postalAddress) {
        LOG.debug("Request to update PostalAddress : {}", postalAddress);
        return postalAddressRepository.save(postalAddress);
    }

    /**
     * Partially update a postalAddress.
     *
     * @param postalAddress the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PostalAddress> partialUpdate(PostalAddress postalAddress) {
        LOG.debug("Request to partially update PostalAddress : {}", postalAddress);

        return postalAddressRepository
            .findById(postalAddress.getId())
            .map(existingPostalAddress -> {
                updateIfPresent(existingPostalAddress::setToName, postalAddress.getToName());
                updateIfPresent(existingPostalAddress::setAddress1, postalAddress.getAddress1());
                updateIfPresent(existingPostalAddress::setAddress2, postalAddress.getAddress2());
                updateIfPresent(existingPostalAddress::setCity, postalAddress.getCity());
                updateIfPresent(existingPostalAddress::setLandmark, postalAddress.getLandmark());
                updateIfPresent(existingPostalAddress::setPostalCode, postalAddress.getPostalCode());
                updateIfPresent(existingPostalAddress::setIsDefault, postalAddress.getIsDefault());
                updateIfPresent(existingPostalAddress::setCustomAddressType, postalAddress.getCustomAddressType());
                updateIfPresent(existingPostalAddress::setStateStr, postalAddress.getStateStr());
                updateIfPresent(existingPostalAddress::setCountryStr, postalAddress.getCountryStr());
                updateIfPresent(existingPostalAddress::setIsIndianAddress, postalAddress.getIsIndianAddress());
                updateIfPresent(existingPostalAddress::setNote, postalAddress.getNote());
                updateIfPresent(existingPostalAddress::setDirections, postalAddress.getDirections());

                return existingPostalAddress;
            })
            .map(postalAddressRepository::save);
    }

    /**
     * Get all the postalAddresses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PostalAddress> findAllWithEagerRelationships(Pageable pageable) {
        return postalAddressRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one postalAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PostalAddress> findOne(Long id) {
        LOG.debug("Request to get PostalAddress : {}", id);
        return postalAddressRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the postalAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PostalAddress : {}", id);
        postalAddressRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
