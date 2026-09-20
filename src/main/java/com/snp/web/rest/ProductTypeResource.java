package com.snp.web.rest;

import com.snp.domain.ProductType;
import com.snp.repository.ProductTypeRepository;
import com.snp.service.ProductTypeQueryService;
import com.snp.service.ProductTypeService;
import com.snp.service.criteria.ProductTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductType}.
 */
@RestController
@RequestMapping("/api/product-types")
public class ProductTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductTypeResource.class);

    private static final String ENTITY_NAME = "productType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductTypeService productTypeService;

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeQueryService productTypeQueryService;

    public ProductTypeResource(
        ProductTypeService productTypeService,
        ProductTypeRepository productTypeRepository,
        ProductTypeQueryService productTypeQueryService
    ) {
        this.productTypeService = productTypeService;
        this.productTypeRepository = productTypeRepository;
        this.productTypeQueryService = productTypeQueryService;
    }

    /**
     * {@code POST  /product-types} : Create a new productType.
     *
     * @param productType the productType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productType, or with status {@code 400 (Bad Request)} if the productType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductType> createProductType(@Valid @RequestBody ProductType productType) throws URISyntaxException {
        LOG.debug("REST request to save ProductType : {}", productType);
        if (productType.getId() != null) {
            throw new BadRequestAlertException("A new productType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productType = productTypeService.save(productType);
        return ResponseEntity.created(new URI("/api/product-types/" + productType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productType.getId().toString()))
            .body(productType);
    }

    /**
     * {@code PUT  /product-types/:id} : Updates an existing productType.
     *
     * @param id the id of the productType to save.
     * @param productType the productType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productType,
     * or with status {@code 400 (Bad Request)} if the productType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductType productType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductType : {}, {}", id, productType);
        if (productType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productType = productTypeService.update(productType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productType.getId().toString()))
            .body(productType);
    }

    /**
     * {@code PATCH  /product-types/:id} : Partial updates given fields of an existing productType, field will ignore if it is null
     *
     * @param id the id of the productType to save.
     * @param productType the productType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productType,
     * or with status {@code 400 (Bad Request)} if the productType is not valid,
     * or with status {@code 404 (Not Found)} if the productType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductType> partialUpdateProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductType productType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductType : {}, {}", id, productType);
        if (productType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductType> result = productTypeService.partialUpdate(productType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-types} : get all the Product Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductType>> getAllProductTypes(ProductTypeCriteria criteria) {
        LOG.debug("REST request to get ProductTypes by criteria: {}", criteria);

        List<ProductType> entityList = productTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-types/count} : count all the productTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductTypes(ProductTypeCriteria criteria) {
        LOG.debug("REST request to count ProductTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-types/:id} : get the "id" productType.
     *
     * @param id the id of the productType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductType> getProductType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductType : {}", id);
        Optional<ProductType> productType = productTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productType);
    }

    /**
     * {@code DELETE  /product-types/:id} : delete the "id" productType.
     *
     * @param id the id of the productType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductType : {}", id);
        productTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
