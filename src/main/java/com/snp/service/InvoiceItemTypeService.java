package com.snp.service;

import com.snp.domain.InvoiceItemType;
import com.snp.repository.InvoiceItemTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InvoiceItemType}.
 */
@Service
@Transactional
public class InvoiceItemTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemTypeService.class);

    private final InvoiceItemTypeRepository invoiceItemTypeRepository;

    public InvoiceItemTypeService(InvoiceItemTypeRepository invoiceItemTypeRepository) {
        this.invoiceItemTypeRepository = invoiceItemTypeRepository;
    }

    /**
     * Save a invoiceItemType.
     *
     * @param invoiceItemType the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItemType save(InvoiceItemType invoiceItemType) {
        LOG.debug("Request to save InvoiceItemType : {}", invoiceItemType);
        return invoiceItemTypeRepository.save(invoiceItemType);
    }

    /**
     * Update a invoiceItemType.
     *
     * @param invoiceItemType the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItemType update(InvoiceItemType invoiceItemType) {
        LOG.debug("Request to update InvoiceItemType : {}", invoiceItemType);
        return invoiceItemTypeRepository.save(invoiceItemType);
    }

    /**
     * Partially update a invoiceItemType.
     *
     * @param invoiceItemType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceItemType> partialUpdate(InvoiceItemType invoiceItemType) {
        LOG.debug("Request to partially update InvoiceItemType : {}", invoiceItemType);

        return invoiceItemTypeRepository
            .findById(invoiceItemType.getId())
            .map(existingInvoiceItemType -> {
                updateIfPresent(existingInvoiceItemType::setName, invoiceItemType.getName());
                updateIfPresent(existingInvoiceItemType::setDescription, invoiceItemType.getDescription());

                return existingInvoiceItemType;
            })
            .map(invoiceItemTypeRepository::save);
    }

    /**
     * Get one invoiceItemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceItemType> findOne(Long id) {
        LOG.debug("Request to get InvoiceItemType : {}", id);
        return invoiceItemTypeRepository.findById(id);
    }

    /**
     * Delete the invoiceItemType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceItemType : {}", id);
        invoiceItemTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
