package com.snp.web.rest;

import com.snp.domain.ProductFeatureApplType;
import com.snp.repository.ProductFeatureApplTypeRepository;
import com.snp.service.ProductFeatureApplTypeQueryService;
import com.snp.service.ProductFeatureApplTypeService;
import com.snp.service.criteria.ProductFeatureApplTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeatureApplType}.
 */
@RestController
@RequestMapping("/api/product-feature-appl-types")
public class ProductFeatureApplTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplTypeResource.class);

    private static final String ENTITY_NAME = "productFeatureApplType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureApplTypeService productFeatureApplTypeService;

    private final ProductFeatureApplTypeRepository productFeatureApplTypeRepository;

    private final ProductFeatureApplTypeQueryService productFeatureApplTypeQueryService;

    public ProductFeatureApplTypeResource(
        ProductFeatureApplTypeService productFeatureApplTypeService,
        ProductFeatureApplTypeRepository productFeatureApplTypeRepository,
        ProductFeatureApplTypeQueryService productFeatureApplTypeQueryService
    ) {
        this.productFeatureApplTypeService = productFeatureApplTypeService;
        this.productFeatureApplTypeRepository = productFeatureApplTypeRepository;
        this.productFeatureApplTypeQueryService = productFeatureApplTypeQueryService;
    }

    /**
     * {@code POST  /product-feature-appl-types} : Create a new productFeatureApplType.
     *
     * @param productFeatureApplType the productFeatureApplType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureApplType, or with status {@code 400 (Bad Request)} if the productFeatureApplType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureApplType> createProductFeatureApplType(
        @Valid @RequestBody ProductFeatureApplType productFeatureApplType
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureApplType : {}", productFeatureApplType);
        if (productFeatureApplType.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureApplType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureApplType = productFeatureApplTypeService.save(productFeatureApplType);
        return ResponseEntity.created(new URI("/api/product-feature-appl-types/" + productFeatureApplType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureApplType.getId().toString()))
            .body(productFeatureApplType);
    }

    /**
     * {@code PUT  /product-feature-appl-types/:id} : Updates an existing productFeatureApplType.
     *
     * @param id the id of the productFeatureApplType to save.
     * @param productFeatureApplType the productFeatureApplType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureApplType,
     * or with status {@code 400 (Bad Request)} if the productFeatureApplType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureApplType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureApplType> updateProductFeatureApplType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureApplType productFeatureApplType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureApplType : {}, {}", id, productFeatureApplType);
        if (productFeatureApplType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureApplType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureApplTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureApplType = productFeatureApplTypeService.update(productFeatureApplType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureApplType.getId().toString()))
            .body(productFeatureApplType);
    }

    /**
     * {@code PATCH  /product-feature-appl-types/:id} : Partial updates given fields of an existing productFeatureApplType, field will ignore if it is null
     *
     * @param id the id of the productFeatureApplType to save.
     * @param productFeatureApplType the productFeatureApplType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureApplType,
     * or with status {@code 400 (Bad Request)} if the productFeatureApplType is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureApplType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureApplType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureApplType> partialUpdateProductFeatureApplType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureApplType productFeatureApplType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureApplType : {}, {}", id, productFeatureApplType);
        if (productFeatureApplType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureApplType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureApplTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureApplType> result = productFeatureApplTypeService.partialUpdate(productFeatureApplType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureApplType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-appl-types} : get all the Product Feature Appl Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Appl Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureApplType>> getAllProductFeatureApplTypes(ProductFeatureApplTypeCriteria criteria) {
        LOG.debug("REST request to get ProductFeatureApplTypes by criteria: {}", criteria);

        List<ProductFeatureApplType> entityList = productFeatureApplTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-feature-appl-types/count} : count all the productFeatureApplTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureApplTypes(ProductFeatureApplTypeCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureApplTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureApplTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-appl-types/:id} : get the "id" productFeatureApplType.
     *
     * @param id the id of the productFeatureApplType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureApplType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureApplType> getProductFeatureApplType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureApplType : {}", id);
        Optional<ProductFeatureApplType> productFeatureApplType = productFeatureApplTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureApplType);
    }

    /**
     * {@code DELETE  /product-feature-appl-types/:id} : delete the "id" productFeatureApplType.
     *
     * @param id the id of the productFeatureApplType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureApplType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureApplType : {}", id);
        productFeatureApplTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
