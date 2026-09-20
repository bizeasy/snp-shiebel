package com.snp.service;

import com.snp.domain.ProductPrice;
import com.snp.repository.ProductPriceRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.ProductPrice}.
 */
@Service
@Transactional
public class ProductPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceService.class);

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    /**
     * Save a productPrice.
     *
     * @param productPrice the entity to save.
     * @return the persisted entity.
     */
    public ProductPrice save(ProductPrice productPrice) {
        LOG.debug("Request to save ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    /**
     * Update a productPrice.
     *
     * @param productPrice the entity to save.
     * @return the persisted entity.
     */
    public ProductPrice update(ProductPrice productPrice) {
        LOG.debug("Request to update ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    /**
     * Partially update a productPrice.
     *
     * @param productPrice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductPrice> partialUpdate(ProductPrice productPrice) {
        LOG.debug("Request to partially update ProductPrice : {}", productPrice);

        return productPriceRepository
            .findById(productPrice.getId())
            .map(existingProductPrice -> {
                updateIfPresent(existingProductPrice::setFromDate, productPrice.getFromDate());
                updateIfPresent(existingProductPrice::setThruDate, productPrice.getThruDate());
                updateIfPresent(existingProductPrice::setPrice, productPrice.getPrice());
                updateIfPresent(existingProductPrice::setCgst, productPrice.getCgst());
                updateIfPresent(existingProductPrice::setIgst, productPrice.getIgst());
                updateIfPresent(existingProductPrice::setSgst, productPrice.getSgst());
                updateIfPresent(existingProductPrice::setTotalPrice, productPrice.getTotalPrice());

                return existingProductPrice;
            })
            .map(productPriceRepository::save);
    }

    /**
     * Get all the productPrices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductPrice> findAllWithEagerRelationships(Pageable pageable) {
        return productPriceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductPrice> findOne(Long id) {
        LOG.debug("Request to get ProductPrice : {}", id);
        return productPriceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productPrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductPrice : {}", id);
        productPriceRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
