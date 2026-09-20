package com.snp.service;

import com.snp.domain.Invoice;
import com.snp.repository.InvoiceRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        LOG.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Update a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice update(Invoice invoice) {
        LOG.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Partially update a invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        LOG.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                updateIfPresent(existingInvoice::setInvoiceDate, invoice.getInvoiceDate());
                updateIfPresent(existingInvoice::setDueDate, invoice.getDueDate());
                updateIfPresent(existingInvoice::setPaidDate, invoice.getPaidDate());
                updateIfPresent(existingInvoice::setReferenceNumber, invoice.getReferenceNumber());
                updateIfPresent(existingInvoice::setInvoiceMessage, invoice.getInvoiceMessage());
                updateIfPresent(existingInvoice::setExternalId, invoice.getExternalId());
                updateIfPresent(existingInvoice::setEntryDate, invoice.getEntryDate());
                updateIfPresent(existingInvoice::setGrandTotal, invoice.getGrandTotal());
                updateIfPresent(existingInvoice::setProjectName, invoice.getProjectName());
                updateIfPresent(existingInvoice::setDescription, invoice.getDescription());
                updateIfPresent(existingInvoice::setRemarks, invoice.getRemarks());
                updateIfPresent(existingInvoice::setPaymentTerms, invoice.getPaymentTerms());
                updateIfPresent(existingInvoice::setDeliveryTerms, invoice.getDeliveryTerms());
                updateIfPresent(existingInvoice::setInfo, invoice.getInfo());

                return existingInvoice;
            })
            .map(invoiceRepository::save);
    }

    /**
     * Get all the invoices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        LOG.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
