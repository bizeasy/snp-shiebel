package com.snp.service;

import com.snp.domain.InvoiceItem;
import com.snp.repository.InvoiceItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.InvoiceItem}.
 */
@Service
@Transactional
public class InvoiceItemService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceItemService.class);

    private final InvoiceItemRepository invoiceItemRepository;

    public InvoiceItemService(InvoiceItemRepository invoiceItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
    }

    /**
     * Save a invoiceItem.
     *
     * @param invoiceItem the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItem save(InvoiceItem invoiceItem) {
        LOG.debug("Request to save InvoiceItem : {}", invoiceItem);
        return invoiceItemRepository.save(invoiceItem);
    }

    /**
     * Update a invoiceItem.
     *
     * @param invoiceItem the entity to save.
     * @return the persisted entity.
     */
    public InvoiceItem update(InvoiceItem invoiceItem) {
        LOG.debug("Request to update InvoiceItem : {}", invoiceItem);
        return invoiceItemRepository.save(invoiceItem);
    }

    /**
     * Partially update a invoiceItem.
     *
     * @param invoiceItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceItem> partialUpdate(InvoiceItem invoiceItem) {
        LOG.debug("Request to partially update InvoiceItem : {}", invoiceItem);

        return invoiceItemRepository
            .findById(invoiceItem.getId())
            .map(existingInvoiceItem -> {
                updateIfPresent(existingInvoiceItem::setSequenceNo, invoiceItem.getSequenceNo());
                updateIfPresent(existingInvoiceItem::setQuantity, invoiceItem.getQuantity());
                updateIfPresent(existingInvoiceItem::setAmount, invoiceItem.getAmount());
                updateIfPresent(existingInvoiceItem::setCancelQuantity, invoiceItem.getCancelQuantity());
                updateIfPresent(existingInvoiceItem::setSelectedAmount, invoiceItem.getSelectedAmount());
                updateIfPresent(existingInvoiceItem::setUnitPrice, invoiceItem.getUnitPrice());
                updateIfPresent(existingInvoiceItem::setUnitListPrice, invoiceItem.getUnitListPrice());
                updateIfPresent(existingInvoiceItem::setIsModifiedPrice, invoiceItem.getIsModifiedPrice());
                updateIfPresent(existingInvoiceItem::setEstimatedShipDate, invoiceItem.getEstimatedShipDate());
                updateIfPresent(existingInvoiceItem::setEstimatedDeliveryDate, invoiceItem.getEstimatedDeliveryDate());
                updateIfPresent(existingInvoiceItem::setShipBeforeDate, invoiceItem.getShipBeforeDate());
                updateIfPresent(existingInvoiceItem::setShipAfterDate, invoiceItem.getShipAfterDate());
                updateIfPresent(existingInvoiceItem::setEntryDate, invoiceItem.getEntryDate());
                updateIfPresent(existingInvoiceItem::setAttr1, invoiceItem.getAttr1());
                updateIfPresent(existingInvoiceItem::setAttr2, invoiceItem.getAttr2());
                updateIfPresent(existingInvoiceItem::setAttr3, invoiceItem.getAttr3());

                return existingInvoiceItem;
            })
            .map(invoiceItemRepository::save);
    }

    /**
     * Get all the invoiceItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InvoiceItem> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one invoiceItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceItem> findOne(Long id) {
        LOG.debug("Request to get InvoiceItem : {}", id);
        return invoiceItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoiceItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceItem : {}", id);
        invoiceItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
