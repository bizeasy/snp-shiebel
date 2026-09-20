package com.snp.service;

import com.snp.domain.ReturnHeader;
import com.snp.repository.ReturnHeaderRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ReturnHeader}.
 */
@Service
@Transactional
public class ReturnHeaderService {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnHeaderService.class);

    private final ReturnHeaderRepository returnHeaderRepository;

    public ReturnHeaderService(ReturnHeaderRepository returnHeaderRepository) {
        this.returnHeaderRepository = returnHeaderRepository;
    }

    /**
     * Save a returnHeader.
     *
     * @param returnHeader the entity to save.
     * @return the persisted entity.
     */
    public ReturnHeader save(ReturnHeader returnHeader) {
        LOG.debug("Request to save ReturnHeader : {}", returnHeader);
        return returnHeaderRepository.save(returnHeader);
    }

    /**
     * Update a returnHeader.
     *
     * @param returnHeader the entity to save.
     * @return the persisted entity.
     */
    public ReturnHeader update(ReturnHeader returnHeader) {
        LOG.debug("Request to update ReturnHeader : {}", returnHeader);
        return returnHeaderRepository.save(returnHeader);
    }

    /**
     * Partially update a returnHeader.
     *
     * @param returnHeader the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReturnHeader> partialUpdate(ReturnHeader returnHeader) {
        LOG.debug("Request to partially update ReturnHeader : {}", returnHeader);

        return returnHeaderRepository
            .findById(returnHeader.getId())
            .map(existingReturnHeader -> {
                updateIfPresent(existingReturnHeader::setNeedsInventoryReceive, returnHeader.getNeedsInventoryReceive());
                updateIfPresent(existingReturnHeader::setSupplierRmaId, returnHeader.getSupplierRmaId());

                return existingReturnHeader;
            })
            .map(returnHeaderRepository::save);
    }

    /**
     * Get all the returnHeaders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReturnHeader> findAllWithEagerRelationships(Pageable pageable) {
        return returnHeaderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one returnHeader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReturnHeader> findOne(Long id) {
        LOG.debug("Request to get ReturnHeader : {}", id);
        return returnHeaderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the returnHeader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReturnHeader : {}", id);
        returnHeaderRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
