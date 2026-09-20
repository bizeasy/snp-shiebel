package com.snp.web.rest;

import com.snp.domain.ProductAssocType;
import com.snp.repository.ProductAssocTypeRepository;
import com.snp.service.ProductAssocTypeQueryService;
import com.snp.service.ProductAssocTypeService;
import com.snp.service.criteria.ProductAssocTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductAssocType}.
 */
@RestController
@RequestMapping("/api/product-assoc-types")
public class ProductAssocTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAssocTypeResource.class);

    private static final String ENTITY_NAME = "productAssocType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductAssocTypeService productAssocTypeService;

    private final ProductAssocTypeRepository productAssocTypeRepository;

    private final ProductAssocTypeQueryService productAssocTypeQueryService;

    public ProductAssocTypeResource(
        ProductAssocTypeService productAssocTypeService,
        ProductAssocTypeRepository productAssocTypeRepository,
        ProductAssocTypeQueryService productAssocTypeQueryService
    ) {
        this.productAssocTypeService = productAssocTypeService;
        this.productAssocTypeRepository = productAssocTypeRepository;
        this.productAssocTypeQueryService = productAssocTypeQueryService;
    }

    /**
     * {@code POST  /product-assoc-types} : Create a new productAssocType.
     *
     * @param productAssocType the productAssocType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAssocType, or with status {@code 400 (Bad Request)} if the productAssocType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAssocType> createProductAssocType(@Valid @RequestBody ProductAssocType productAssocType)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductAssocType : {}", productAssocType);
        if (productAssocType.getId() != null) {
            throw new BadRequestAlertException("A new productAssocType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAssocType = productAssocTypeService.save(productAssocType);
        return ResponseEntity.created(new URI("/api/product-assoc-types/" + productAssocType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAssocType.getId().toString()))
            .body(productAssocType);
    }

    /**
     * {@code PUT  /product-assoc-types/:id} : Updates an existing productAssocType.
     *
     * @param id the id of the productAssocType to save.
     * @param productAssocType the productAssocType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssocType,
     * or with status {@code 400 (Bad Request)} if the productAssocType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAssocType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAssocType> updateProductAssocType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductAssocType productAssocType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAssocType : {}, {}", id, productAssocType);
        if (productAssocType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssocType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssocTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAssocType = productAssocTypeService.update(productAssocType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssocType.getId().toString()))
            .body(productAssocType);
    }

    /**
     * {@code PATCH  /product-assoc-types/:id} : Partial updates given fields of an existing productAssocType, field will ignore if it is null
     *
     * @param id the id of the productAssocType to save.
     * @param productAssocType the productAssocType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAssocType,
     * or with status {@code 400 (Bad Request)} if the productAssocType is not valid,
     * or with status {@code 404 (Not Found)} if the productAssocType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAssocType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAssocType> partialUpdateProductAssocType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductAssocType productAssocType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductAssocType : {}, {}", id, productAssocType);
        if (productAssocType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAssocType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAssocTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAssocType> result = productAssocTypeService.partialUpdate(productAssocType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAssocType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-assoc-types} : get all the Product Assoc Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Assoc Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductAssocType>> getAllProductAssocTypes(ProductAssocTypeCriteria criteria) {
        LOG.debug("REST request to get ProductAssocTypes by criteria: {}", criteria);

        List<ProductAssocType> entityList = productAssocTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-assoc-types/count} : count all the productAssocTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductAssocTypes(ProductAssocTypeCriteria criteria) {
        LOG.debug("REST request to count ProductAssocTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productAssocTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-assoc-types/:id} : get the "id" productAssocType.
     *
     * @param id the id of the productAssocType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAssocType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAssocType> getProductAssocType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAssocType : {}", id);
        Optional<ProductAssocType> productAssocType = productAssocTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productAssocType);
    }

    /**
     * {@code DELETE  /product-assoc-types/:id} : delete the "id" productAssocType.
     *
     * @param id the id of the productAssocType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAssocType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAssocType : {}", id);
        productAssocTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
