package com.snp.service;

import com.snp.domain.InvoiceType;
import com.snp.repository.InvoiceTypeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InvoiceType}.
 */
@Service
@Transactional
public class InvoiceTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceTypeService.class);

    private final InvoiceTypeRepository invoiceTypeRepository;

    public InvoiceTypeService(InvoiceTypeRepository invoiceTypeRepository) {
        this.invoiceTypeRepository = invoiceTypeRepository;
    }

    /**
     * Save a invoiceType.
     *
     * @param invoiceType the entity to save.
     * @return the persisted entity.
     */
    public InvoiceType save(InvoiceType invoiceType) {
        LOG.debug("Request to save InvoiceType : {}", invoiceType);
        return invoiceTypeRepository.save(invoiceType);
    }

    /**
     * Update a invoiceType.
     *
     * @param invoiceType the entity to save.
     * @return the persisted entity.
     */
    public InvoiceType update(InvoiceType invoiceType) {
        LOG.debug("Request to update InvoiceType : {}", invoiceType);
        return invoiceTypeRepository.save(invoiceType);
    }

    /**
     * Partially update a invoiceType.
     *
     * @param invoiceType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceType> partialUpdate(InvoiceType invoiceType) {
        LOG.debug("Request to partially update InvoiceType : {}", invoiceType);

        return invoiceTypeRepository
            .findById(invoiceType.getId())
            .map(existingInvoiceType -> {
                updateIfPresent(existingInvoiceType::setName, invoiceType.getName());
                updateIfPresent(existingInvoiceType::setDescription, invoiceType.getDescription());

                return existingInvoiceType;
            })
            .map(invoiceTypeRepository::save);
    }

    /**
     * Get one invoiceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceType> findOne(Long id) {
        LOG.debug("Request to get InvoiceType : {}", id);
        return invoiceTypeRepository.findById(id);
    }

    /**
     * Delete the invoiceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceType : {}", id);
        invoiceTypeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
