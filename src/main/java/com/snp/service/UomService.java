package com.snp.service;

import com.snp.domain.Uom;
import com.snp.repository.UomRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Uom}.
 */
@Service
@Transactional
public class UomService {

    private static final Logger LOG = LoggerFactory.getLogger(UomService.class);

    private final UomRepository uomRepository;

    public UomService(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    /**
     * Save a uom.
     *
     * @param uom the entity to save.
     * @return the persisted entity.
     */
    public Uom save(Uom uom) {
        LOG.debug("Request to save Uom : {}", uom);
        return uomRepository.save(uom);
    }

    /**
     * Update a uom.
     *
     * @param uom the entity to save.
     * @return the persisted entity.
     */
    public Uom update(Uom uom) {
        LOG.debug("Request to update Uom : {}", uom);
        return uomRepository.save(uom);
    }

    /**
     * Partially update a uom.
     *
     * @param uom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Uom> partialUpdate(Uom uom) {
        LOG.debug("Request to partially update Uom : {}", uom);

        return uomRepository
            .findById(uom.getId())
            .map(existingUom -> {
                updateIfPresent(existingUom::setName, uom.getName());
                updateIfPresent(existingUom::setDescription, uom.getDescription());
                updateIfPresent(existingUom::setSequenceNo, uom.getSequenceNo());
                updateIfPresent(existingUom::setAbbreviation, uom.getAbbreviation());

                return existingUom;
            })
            .map(uomRepository::save);
    }

    /**
     * Get all the uoms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<Uom> findAllWithEagerRelationships() {
        return uomRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one uom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Uom> findOne(Long id) {
        LOG.debug("Request to get Uom : {}", id);
        return uomRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the uom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Uom : {}", id);
        uomRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
