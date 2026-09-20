package com.snp.service;

import com.snp.domain.Product;
import com.snp.repository.ProductRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.snp.domain.Product}.
 */
@Service
@Transactional
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product save(Product product) {
        LOG.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Update a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product update(Product product) {
        LOG.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Product> partialUpdate(Product product) {
        LOG.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                updateIfPresent(existingProduct::setName, product.getName());
                updateIfPresent(existingProduct::setInternalName, product.getInternalName());
                updateIfPresent(existingProduct::setBrandName, product.getBrandName());
                updateIfPresent(existingProduct::setVariant, product.getVariant());
                updateIfPresent(existingProduct::setSku, product.getSku());
                updateIfPresent(existingProduct::setIntroductionDate, product.getIntroductionDate());
                updateIfPresent(existingProduct::setReleaseDate, product.getReleaseDate());
                updateIfPresent(existingProduct::setQuantityIncluded, product.getQuantityIncluded());
                updateIfPresent(existingProduct::setPiecesIncluded, product.getPiecesIncluded());
                updateIfPresent(existingProduct::setWeight, product.getWeight());
                updateIfPresent(existingProduct::setHeight, product.getHeight());
                updateIfPresent(existingProduct::setWidth, product.getWidth());
                updateIfPresent(existingProduct::setDepth, product.getDepth());
                updateIfPresent(existingProduct::setSmallImageUrl, product.getSmallImageUrl());
                updateIfPresent(existingProduct::setMediumImageUrl, product.getMediumImageUrl());
                updateIfPresent(existingProduct::setLargeImageUrl, product.getLargeImageUrl());
                updateIfPresent(existingProduct::setDetailImageUrl, product.getDetailImageUrl());
                updateIfPresent(existingProduct::setOriginalImageUrl, product.getOriginalImageUrl());
                updateIfPresent(existingProduct::setAltImageUrl1, product.getAltImageUrl1());
                updateIfPresent(existingProduct::setAltImageUrl2, product.getAltImageUrl2());
                updateIfPresent(existingProduct::setAltImageUrl3, product.getAltImageUrl3());
                updateIfPresent(existingProduct::setAltImageUrl4, product.getAltImageUrl4());
                updateIfPresent(existingProduct::setAltImageUrl5, product.getAltImageUrl5());
                updateIfPresent(existingProduct::setAltImageUrl6, product.getAltImageUrl6());
                updateIfPresent(existingProduct::setVideoUrl, product.getVideoUrl());
                updateIfPresent(existingProduct::setQuantity, product.getQuantity());
                updateIfPresent(existingProduct::setPrice, product.getPrice());
                updateIfPresent(existingProduct::setCgst, product.getCgst());
                updateIfPresent(existingProduct::setIgst, product.getIgst());
                updateIfPresent(existingProduct::setSgst, product.getSgst());
                updateIfPresent(existingProduct::setCgstPercentage, product.getCgstPercentage());
                updateIfPresent(existingProduct::setIgstPercentage, product.getIgstPercentage());
                updateIfPresent(existingProduct::setSgstPercentage, product.getSgstPercentage());
                updateIfPresent(existingProduct::setTotalPrice, product.getTotalPrice());
                updateIfPresent(existingProduct::setDescription, product.getDescription());
                updateIfPresent(existingProduct::setLongDescription, product.getLongDescription());
                updateIfPresent(existingProduct::setInfo, product.getInfo());
                updateIfPresent(existingProduct::setIsVirtual, product.getIsVirtual());
                updateIfPresent(existingProduct::setIsVariant, product.getIsVariant());
                updateIfPresent(existingProduct::setRequireInventory, product.getRequireInventory());
                updateIfPresent(existingProduct::setReturnable, product.getReturnable());
                updateIfPresent(existingProduct::setIsPopular, product.getIsPopular());
                updateIfPresent(existingProduct::setIsBanner, product.getIsBanner());
                updateIfPresent(existingProduct::setBannerUrl, product.getBannerUrl());

                return existingProduct;
            })
            .map(productRepository::save);
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
