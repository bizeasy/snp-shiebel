package com.snp.service;

import com.snp.domain.Catalogue;
import com.snp.repository.CatalogueRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Catalogue}.
 */
@Service
@Transactional
public class CatalogueService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueService.class);

    private final CatalogueRepository catalogueRepository;

    public CatalogueService(CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    /**
     * Save a catalogue.
     *
     * @param catalogue the entity to save.
     * @return the persisted entity.
     */
    public Catalogue save(Catalogue catalogue) {
        LOG.debug("Request to save Catalogue : {}", catalogue);
        return catalogueRepository.save(catalogue);
    }

    /**
     * Update a catalogue.
     *
     * @param catalogue the entity to save.
     * @return the persisted entity.
     */
    public Catalogue update(Catalogue catalogue) {
        LOG.debug("Request to update Catalogue : {}", catalogue);
        return catalogueRepository.save(catalogue);
    }

    /**
     * Partially update a catalogue.
     *
     * @param catalogue the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Catalogue> partialUpdate(Catalogue catalogue) {
        LOG.debug("Request to partially update Catalogue : {}", catalogue);

        return catalogueRepository
            .findById(catalogue.getId())
            .map(existingCatalogue -> {
                updateIfPresent(existingCatalogue::setName, catalogue.getName());
                updateIfPresent(existingCatalogue::setDescription, catalogue.getDescription());
                updateIfPresent(existingCatalogue::setSequenceNo, catalogue.getSequenceNo());
                updateIfPresent(existingCatalogue::setImagePath, catalogue.getImagePath());
                updateIfPresent(existingCatalogue::setMobileImagePath, catalogue.getMobileImagePath());
                updateIfPresent(existingCatalogue::setAltImage1, catalogue.getAltImage1());
                updateIfPresent(existingCatalogue::setAltImage2, catalogue.getAltImage2());
                updateIfPresent(existingCatalogue::setAltImage3, catalogue.getAltImage3());
                updateIfPresent(existingCatalogue::setBannerUrl, catalogue.getBannerUrl());
                updateIfPresent(existingCatalogue::setIsBanner, catalogue.getIsBanner());

                return existingCatalogue;
            })
            .map(catalogueRepository::save);
    }

    /**
     * Get one catalogue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Catalogue> findOne(Long id) {
        LOG.debug("Request to get Catalogue : {}", id);
        return catalogueRepository.findById(id);
    }

    /**
     * Delete the catalogue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Catalogue : {}", id);
        catalogueRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
