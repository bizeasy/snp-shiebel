package com.snp.web.rest;

import com.snp.domain.ProductFeatureType;
import com.snp.repository.ProductFeatureTypeRepository;
import com.snp.service.ProductFeatureTypeQueryService;
import com.snp.service.ProductFeatureTypeService;
import com.snp.service.criteria.ProductFeatureTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeatureType}.
 */
@RestController
@RequestMapping("/api/product-feature-types")
public class ProductFeatureTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureTypeResource.class);

    private static final String ENTITY_NAME = "productFeatureType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureTypeService productFeatureTypeService;

    private final ProductFeatureTypeRepository productFeatureTypeRepository;

    private final ProductFeatureTypeQueryService productFeatureTypeQueryService;

    public ProductFeatureTypeResource(
        ProductFeatureTypeService productFeatureTypeService,
        ProductFeatureTypeRepository productFeatureTypeRepository,
        ProductFeatureTypeQueryService productFeatureTypeQueryService
    ) {
        this.productFeatureTypeService = productFeatureTypeService;
        this.productFeatureTypeRepository = productFeatureTypeRepository;
        this.productFeatureTypeQueryService = productFeatureTypeQueryService;
    }

    /**
     * {@code POST  /product-feature-types} : Create a new productFeatureType.
     *
     * @param productFeatureType the productFeatureType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureType, or with status {@code 400 (Bad Request)} if the productFeatureType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureType> createProductFeatureType(@Valid @RequestBody ProductFeatureType productFeatureType)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureType : {}", productFeatureType);
        if (productFeatureType.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureType = productFeatureTypeService.save(productFeatureType);
        return ResponseEntity.created(new URI("/api/product-feature-types/" + productFeatureType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureType.getId().toString()))
            .body(productFeatureType);
    }

    /**
     * {@code PUT  /product-feature-types/:id} : Updates an existing productFeatureType.
     *
     * @param id the id of the productFeatureType to save.
     * @param productFeatureType the productFeatureType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureType,
     * or with status {@code 400 (Bad Request)} if the productFeatureType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureType> updateProductFeatureType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureType productFeatureType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureType : {}, {}", id, productFeatureType);
        if (productFeatureType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureType = productFeatureTypeService.update(productFeatureType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureType.getId().toString()))
            .body(productFeatureType);
    }

    /**
     * {@code PATCH  /product-feature-types/:id} : Partial updates given fields of an existing productFeatureType, field will ignore if it is null
     *
     * @param id the id of the productFeatureType to save.
     * @param productFeatureType the productFeatureType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureType,
     * or with status {@code 400 (Bad Request)} if the productFeatureType is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureType> partialUpdateProductFeatureType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureType productFeatureType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureType : {}, {}", id, productFeatureType);
        if (productFeatureType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureType> result = productFeatureTypeService.partialUpdate(productFeatureType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-types} : get all the Product Feature Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureType>> getAllProductFeatureTypes(ProductFeatureTypeCriteria criteria) {
        LOG.debug("REST request to get ProductFeatureTypes by criteria: {}", criteria);

        List<ProductFeatureType> entityList = productFeatureTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-feature-types/count} : count all the productFeatureTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureTypes(ProductFeatureTypeCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-types/:id} : get the "id" productFeatureType.
     *
     * @param id the id of the productFeatureType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureType> getProductFeatureType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureType : {}", id);
        Optional<ProductFeatureType> productFeatureType = productFeatureTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureType);
    }

    /**
     * {@code DELETE  /product-feature-types/:id} : delete the "id" productFeatureType.
     *
     * @param id the id of the productFeatureType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureType : {}", id);
        productFeatureTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
