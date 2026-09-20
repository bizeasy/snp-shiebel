package com.snp.service;

import com.snp.domain.SupplierProduct;
import com.snp.repository.SupplierProductRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.SupplierProduct}.
 */
@Service
@Transactional
public class SupplierProductService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierProductService.class);

    private final SupplierProductRepository supplierProductRepository;

    public SupplierProductService(SupplierProductRepository supplierProductRepository) {
        this.supplierProductRepository = supplierProductRepository;
    }

    /**
     * Save a supplierProduct.
     *
     * @param supplierProduct the entity to save.
     * @return the persisted entity.
     */
    public SupplierProduct save(SupplierProduct supplierProduct) {
        LOG.debug("Request to save SupplierProduct : {}", supplierProduct);
        return supplierProductRepository.save(supplierProduct);
    }

    /**
     * Update a supplierProduct.
     *
     * @param supplierProduct the entity to save.
     * @return the persisted entity.
     */
    public SupplierProduct update(SupplierProduct supplierProduct) {
        LOG.debug("Request to update SupplierProduct : {}", supplierProduct);
        return supplierProductRepository.save(supplierProduct);
    }

    /**
     * Partially update a supplierProduct.
     *
     * @param supplierProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierProduct> partialUpdate(SupplierProduct supplierProduct) {
        LOG.debug("Request to partially update SupplierProduct : {}", supplierProduct);

        return supplierProductRepository
            .findById(supplierProduct.getId())
            .map(existingSupplierProduct -> {
                updateIfPresent(existingSupplierProduct::setFromDate, supplierProduct.getFromDate());
                updateIfPresent(existingSupplierProduct::setThruDate, supplierProduct.getThruDate());
                updateIfPresent(existingSupplierProduct::setSupplierProductId, supplierProduct.getSupplierProductId());
                updateIfPresent(existingSupplierProduct::setMinOrderQty, supplierProduct.getMinOrderQty());
                updateIfPresent(existingSupplierProduct::setLastPrice, supplierProduct.getLastPrice());
                updateIfPresent(existingSupplierProduct::setShippingPrice, supplierProduct.getShippingPrice());
                updateIfPresent(existingSupplierProduct::setCgst, supplierProduct.getCgst());
                updateIfPresent(existingSupplierProduct::setIgst, supplierProduct.getIgst());
                updateIfPresent(existingSupplierProduct::setSgst, supplierProduct.getSgst());
                updateIfPresent(existingSupplierProduct::setTotalPrice, supplierProduct.getTotalPrice());
                updateIfPresent(existingSupplierProduct::setComments, supplierProduct.getComments());
                updateIfPresent(existingSupplierProduct::setLeadTimeDays, supplierProduct.getLeadTimeDays());
                updateIfPresent(existingSupplierProduct::setUnitsIncluded, supplierProduct.getUnitsIncluded());
                updateIfPresent(existingSupplierProduct::setCanDropShip, supplierProduct.getCanDropShip());

                return existingSupplierProduct;
            })
            .map(supplierProductRepository::save);
    }

    /**
     * Get all the supplierProducts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierProduct> findAllWithEagerRelationships(Pageable pageable) {
        return supplierProductRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierProduct> findOne(Long id) {
        LOG.debug("Request to get SupplierProduct : {}", id);
        return supplierProductRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierProduct : {}", id);
        supplierProductRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
