package com.snp.web.rest;

import com.snp.domain.ProductCategoryType;
import com.snp.repository.ProductCategoryTypeRepository;
import com.snp.service.ProductCategoryTypeQueryService;
import com.snp.service.ProductCategoryTypeService;
import com.snp.service.criteria.ProductCategoryTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductCategoryType}.
 */
@RestController
@RequestMapping("/api/product-category-types")
public class ProductCategoryTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryTypeResource.class);

    private static final String ENTITY_NAME = "productCategoryType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductCategoryTypeService productCategoryTypeService;

    private final ProductCategoryTypeRepository productCategoryTypeRepository;

    private final ProductCategoryTypeQueryService productCategoryTypeQueryService;

    public ProductCategoryTypeResource(
        ProductCategoryTypeService productCategoryTypeService,
        ProductCategoryTypeRepository productCategoryTypeRepository,
        ProductCategoryTypeQueryService productCategoryTypeQueryService
    ) {
        this.productCategoryTypeService = productCategoryTypeService;
        this.productCategoryTypeRepository = productCategoryTypeRepository;
        this.productCategoryTypeQueryService = productCategoryTypeQueryService;
    }

    /**
     * {@code POST  /product-category-types} : Create a new productCategoryType.
     *
     * @param productCategoryType the productCategoryType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCategoryType, or with status {@code 400 (Bad Request)} if the productCategoryType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductCategoryType> createProductCategoryType(@Valid @RequestBody ProductCategoryType productCategoryType)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductCategoryType : {}", productCategoryType);
        if (productCategoryType.getId() != null) {
            throw new BadRequestAlertException("A new productCategoryType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productCategoryType = productCategoryTypeService.save(productCategoryType);
        return ResponseEntity.created(new URI("/api/product-category-types/" + productCategoryType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productCategoryType.getId().toString()))
            .body(productCategoryType);
    }

    /**
     * {@code PUT  /product-category-types/:id} : Updates an existing productCategoryType.
     *
     * @param id the id of the productCategoryType to save.
     * @param productCategoryType the productCategoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategoryType,
     * or with status {@code 400 (Bad Request)} if the productCategoryType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCategoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryType> updateProductCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductCategoryType productCategoryType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductCategoryType : {}, {}", id, productCategoryType);
        if (productCategoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productCategoryType = productCategoryTypeService.update(productCategoryType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategoryType.getId().toString()))
            .body(productCategoryType);
    }

    /**
     * {@code PATCH  /product-category-types/:id} : Partial updates given fields of an existing productCategoryType, field will ignore if it is null
     *
     * @param id the id of the productCategoryType to save.
     * @param productCategoryType the productCategoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategoryType,
     * or with status {@code 400 (Bad Request)} if the productCategoryType is not valid,
     * or with status {@code 404 (Not Found)} if the productCategoryType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCategoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductCategoryType> partialUpdateProductCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductCategoryType productCategoryType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductCategoryType : {}, {}", id, productCategoryType);
        if (productCategoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCategoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCategoryType> result = productCategoryTypeService.partialUpdate(productCategoryType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCategoryType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-category-types} : get all the Product Category Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Category Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductCategoryType>> getAllProductCategoryTypes(ProductCategoryTypeCriteria criteria) {
        LOG.debug("REST request to get ProductCategoryTypes by criteria: {}", criteria);

        List<ProductCategoryType> entityList = productCategoryTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-category-types/count} : count all the productCategoryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductCategoryTypes(ProductCategoryTypeCriteria criteria) {
        LOG.debug("REST request to count ProductCategoryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productCategoryTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-category-types/:id} : get the "id" productCategoryType.
     *
     * @param id the id of the productCategoryType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCategoryType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryType> getProductCategoryType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductCategoryType : {}", id);
        Optional<ProductCategoryType> productCategoryType = productCategoryTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCategoryType);
    }

    /**
     * {@code DELETE  /product-category-types/:id} : delete the "id" productCategoryType.
     *
     * @param id the id of the productCategoryType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategoryType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductCategoryType : {}", id);
        productCategoryTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
