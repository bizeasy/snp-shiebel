package com.snp.service;

import com.snp.domain.CustRequestType;
import com.snp.repository.CustRequestTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.CustRequestType}.
 */
@Service
@Transactional
public class CustRequestTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CustRequestTypeService.class);

    private final CustRequestTypeRepository custRequestTypeRepository;

    public CustRequestTypeService(CustRequestTypeRepository custRequestTypeRepository) {
        this.custRequestTypeRepository = custRequestTypeRepository;
    }

    /**
     * Save a custRequestType.
     *
     * @param custRequestType the entity to save.
     * @return the persisted entity.
     */
    public CustRequestType save(CustRequestType custRequestType) {
        LOG.debug("Request to save CustRequestType : {}", custRequestType);
        return custRequestTypeRepository.save(custRequestType);
    }

    /**
     * Update a custRequestType.
     *
     * @param custRequestType the entity to save.
     * @return the persisted entity.
     */
    public CustRequestType update(CustRequestType custRequestType) {
        LOG.debug("Request to update CustRequestType : {}", custRequestType);
        return custRequestTypeRepository.save(custRequestType);
    }

    /**
     * Partially update a custRequestType.
     *
     * @param custRequestType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustRequestType> partialUpdate(CustRequestType custRequestType) {
        LOG.debug("Request to partially update CustRequestType : {}", custRequestType);

        return custRequestTypeRepository
            .findById(custRequestType.getId())
            .map(existingCustRequestType -> {
                updateIfPresent(existingCustRequestType::setName, custRequestType.getName());
                updateIfPresent(existingCustRequestType::setDescription, custRequestType.getDescription());

                return existingCustRequestType;
            })
            .map(custRequestTypeRepository::save);
    }

    /**
     * Get all the custRequestTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<CustRequestType> findAllWithEagerRelationships() {
        return custRequestTypeRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one custRequestType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustRequestType> findOne(Long id) {
        LOG.debug("Request to get CustRequestType : {}", id);
        return custRequestTypeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the custRequestType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustRequestType : {}", id);
        custRequestTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
