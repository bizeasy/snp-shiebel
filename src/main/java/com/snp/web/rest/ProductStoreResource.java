package com.snp.web.rest;

import com.snp.domain.ProductStore;
import com.snp.repository.ProductStoreRepository;
import com.snp.service.ProductStoreQueryService;
import com.snp.service.ProductStoreService;
import com.snp.service.criteria.ProductStoreCriteria;
import com.snp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.ProductStore}.
 */
@RestController
@RequestMapping("/api/product-stores")
public class ProductStoreResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStoreResource.class);

    private static final String ENTITY_NAME = "productStore";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductStoreService productStoreService;

    private final ProductStoreRepository productStoreRepository;

    private final ProductStoreQueryService productStoreQueryService;

    public ProductStoreResource(
        ProductStoreService productStoreService,
        ProductStoreRepository productStoreRepository,
        ProductStoreQueryService productStoreQueryService
    ) {
        this.productStoreService = productStoreService;
        this.productStoreRepository = productStoreRepository;
        this.productStoreQueryService = productStoreQueryService;
    }

    /**
     * {@code POST  /product-stores} : Create a new productStore.
     *
     * @param productStore the productStore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productStore, or with status {@code 400 (Bad Request)} if the productStore already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductStore> createProductStore(@Valid @RequestBody ProductStore productStore) throws URISyntaxException {
        LOG.debug("REST request to save ProductStore : {}", productStore);
        if (productStore.getId() != null) {
            throw new BadRequestAlertException("A new productStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productStore = productStoreService.save(productStore);
        return ResponseEntity.created(new URI("/api/product-stores/" + productStore.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productStore.getId().toString()))
            .body(productStore);
    }

    /**
     * {@code PUT  /product-stores/:id} : Updates an existing productStore.
     *
     * @param id the id of the productStore to save.
     * @param productStore the productStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStore,
     * or with status {@code 400 (Bad Request)} if the productStore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductStore> updateProductStore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductStore productStore
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductStore : {}, {}", id, productStore);
        if (productStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productStore = productStoreService.update(productStore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productStore.getId().toString()))
            .body(productStore);
    }

    /**
     * {@code PATCH  /product-stores/:id} : Partial updates given fields of an existing productStore, field will ignore if it is null
     *
     * @param id the id of the productStore to save.
     * @param productStore the productStore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStore,
     * or with status {@code 400 (Bad Request)} if the productStore is not valid,
     * or with status {@code 404 (Not Found)} if the productStore is not found,
     * or with status {@code 500 (Internal Server Error)} if the productStore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductStore> partialUpdateProductStore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductStore productStore
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductStore : {}, {}", id, productStore);
        if (productStore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductStore> result = productStoreService.partialUpdate(productStore);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productStore.getId().toString())
        );
    }

    /**
     * {@code GET  /product-stores} : get all the Product Stores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Stores in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductStore>> getAllProductStores(ProductStoreCriteria criteria) {
        LOG.debug("REST request to get ProductStores by criteria: {}", criteria);

        List<ProductStore> entityList = productStoreQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-stores/count} : count all the productStores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductStores(ProductStoreCriteria criteria) {
        LOG.debug("REST request to count ProductStores by criteria: {}", criteria);
        return ResponseEntity.ok().body(productStoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-stores/:id} : get the "id" productStore.
     *
     * @param id the id of the productStore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productStore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductStore> getProductStore(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductStore : {}", id);
        Optional<ProductStore> productStore = productStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productStore);
    }

    /**
     * {@code DELETE  /product-stores/:id} : delete the "id" productStore.
     *
     * @param id the id of the productStore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductStore(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductStore : {}", id);
        productStoreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
