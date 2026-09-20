package com.snp.web.rest;

import com.snp.domain.ProductPricePurpose;
import com.snp.repository.ProductPricePurposeRepository;
import com.snp.service.ProductPricePurposeQueryService;
import com.snp.service.ProductPricePurposeService;
import com.snp.service.criteria.ProductPricePurposeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductPricePurpose}.
 */
@RestController
@RequestMapping("/api/product-price-purposes")
public class ProductPricePurposeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPricePurposeResource.class);

    private static final String ENTITY_NAME = "productPricePurpose";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductPricePurposeService productPricePurposeService;

    private final ProductPricePurposeRepository productPricePurposeRepository;

    private final ProductPricePurposeQueryService productPricePurposeQueryService;

    public ProductPricePurposeResource(
        ProductPricePurposeService productPricePurposeService,
        ProductPricePurposeRepository productPricePurposeRepository,
        ProductPricePurposeQueryService productPricePurposeQueryService
    ) {
        this.productPricePurposeService = productPricePurposeService;
        this.productPricePurposeRepository = productPricePurposeRepository;
        this.productPricePurposeQueryService = productPricePurposeQueryService;
    }

    /**
     * {@code POST  /product-price-purposes} : Create a new productPricePurpose.
     *
     * @param productPricePurpose the productPricePurpose to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPricePurpose, or with status {@code 400 (Bad Request)} if the productPricePurpose already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductPricePurpose> createProductPricePurpose(@Valid @RequestBody ProductPricePurpose productPricePurpose)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductPricePurpose : {}", productPricePurpose);
        if (productPricePurpose.getId() != null) {
            throw new BadRequestAlertException("A new productPricePurpose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productPricePurpose = productPricePurposeService.save(productPricePurpose);
        return ResponseEntity.created(new URI("/api/product-price-purposes/" + productPricePurpose.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productPricePurpose.getId().toString()))
            .body(productPricePurpose);
    }

    /**
     * {@code PUT  /product-price-purposes/:id} : Updates an existing productPricePurpose.
     *
     * @param id the id of the productPricePurpose to save.
     * @param productPricePurpose the productPricePurpose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPricePurpose,
     * or with status {@code 400 (Bad Request)} if the productPricePurpose is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPricePurpose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductPricePurpose> updateProductPricePurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductPricePurpose productPricePurpose
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductPricePurpose : {}, {}", id, productPricePurpose);
        if (productPricePurpose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPricePurpose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPricePurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productPricePurpose = productPricePurposeService.update(productPricePurpose);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPricePurpose.getId().toString()))
            .body(productPricePurpose);
    }

    /**
     * {@code PATCH  /product-price-purposes/:id} : Partial updates given fields of an existing productPricePurpose, field will ignore if it is null
     *
     * @param id the id of the productPricePurpose to save.
     * @param productPricePurpose the productPricePurpose to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPricePurpose,
     * or with status {@code 400 (Bad Request)} if the productPricePurpose is not valid,
     * or with status {@code 404 (Not Found)} if the productPricePurpose is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPricePurpose couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPricePurpose> partialUpdateProductPricePurpose(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductPricePurpose productPricePurpose
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductPricePurpose : {}, {}", id, productPricePurpose);
        if (productPricePurpose.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPricePurpose.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPricePurposeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPricePurpose> result = productPricePurposeService.partialUpdate(productPricePurpose);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPricePurpose.getId().toString())
        );
    }

    /**
     * {@code GET  /product-price-purposes} : get all the Product Price Purposes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Price Purposes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductPricePurpose>> getAllProductPricePurposes(ProductPricePurposeCriteria criteria) {
        LOG.debug("REST request to get ProductPricePurposes by criteria: {}", criteria);

        List<ProductPricePurpose> entityList = productPricePurposeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-price-purposes/count} : count all the productPricePurposes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductPricePurposes(ProductPricePurposeCriteria criteria) {
        LOG.debug("REST request to count ProductPricePurposes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productPricePurposeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-price-purposes/:id} : get the "id" productPricePurpose.
     *
     * @param id the id of the productPricePurpose to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPricePurpose, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductPricePurpose> getProductPricePurpose(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductPricePurpose : {}", id);
        Optional<ProductPricePurpose> productPricePurpose = productPricePurposeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPricePurpose);
    }

    /**
     * {@code DELETE  /product-price-purposes/:id} : delete the "id" productPricePurpose.
     *
     * @param id the id of the productPricePurpose to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPricePurpose(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductPricePurpose : {}", id);
        productPricePurposeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
