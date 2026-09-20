package com.snp.service;

import com.snp.domain.InvoiceItemTypeMap;
import com.snp.repository.InvoiceItemTypeMapRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InvoiceItemTypeMap}.
 */
@Service
@Transactional
public class InvoiceItemTypeMapService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeMapService.class);

    private final InvoiceItemTypeMapRepository invoiceItemTypeMapRepository;

    public InvoiceItemTypeMapService(InvoiceItemTypeMapRepository invoiceItemTypeMapRepository) {
        this.invoiceItemTypeMapRepository = invoiceItemTypeMapRepository;
    }

    /**
     * Save a invoiceItemTypeMap.
     *
     * @param invoiceItemTypeMap the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItemTypeMap save(InvoiceItemTypeMap invoiceItemTypeMap) {
        LOG.debug("Request to save InvoiceItemTypeMap : {}", invoiceItemTypeMap);
        return invoiceItemTypeMapRepository.save(invoiceItemTypeMap);
    }

    /**
     * Update a invoiceItemTypeMap.
     *
     * @param invoiceItemTypeMap the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItemTypeMap update(InvoiceItemTypeMap invoiceItemTypeMap) {
        LOG.debug("Request to update InvoiceItemTypeMap : {}", invoiceItemTypeMap);
        return invoiceItemTypeMapRepository.save(invoiceItemTypeMap);
    }

    /**
     * Partially update a invoiceItemTypeMap.
     *
     * @param invoiceItemTypeMap the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceItemTypeMap> partialUpdate(InvoiceItemTypeMap invoiceItemTypeMap) {
        LOG.debug("Request to partially update InvoiceItemTypeMap : {}", invoiceItemTypeMap);

        return invoiceItemTypeMapRepository
            .findById(invoiceItemTypeMap.getId())
            .map(existingInvoiceItemTypeMap -> {
                updateIfPresent(existingInvoiceItemTypeMap::setInvoiceItemMapKey, invoiceItemTypeMap.getInvoiceItemMapKey());

                return existingInvoiceItemTypeMap;
            })
            .map(invoiceItemTypeMapRepository::save);
    }

    /**
     * Get all the invoiceItemTypeMaps with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<InvoiceItemTypeMap> findAllWithEagerRelationships() {
        return invoiceItemTypeMapRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one invoiceItemTypeMap by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceItemTypeMap> findOne(Long id) {
        LOG.debug("Request to get InvoiceItemTypeMap : {}", id);
        return invoiceItemTypeMapRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoiceItemTypeMap by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceItemTypeMap : {}", id);
        invoiceItemTypeMapRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
