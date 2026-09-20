package com.snp.web.rest;

import com.snp.domain.ProductPriceType;
import com.snp.repository.ProductPriceTypeRepository;
import com.snp.service.ProductPriceTypeQueryService;
import com.snp.service.ProductPriceTypeService;
import com.snp.service.criteria.ProductPriceTypeCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductPriceType}.
 */
@RestController
@RequestMapping("/api/product-price-types")
public class ProductPriceTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceTypeResource.class);

    private static final String ENTITY_NAME = "productPriceType";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductPriceTypeService productPriceTypeService;

    private final ProductPriceTypeRepository productPriceTypeRepository;

    private final ProductPriceTypeQueryService productPriceTypeQueryService;

    public ProductPriceTypeResource(
        ProductPriceTypeService productPriceTypeService,
        ProductPriceTypeRepository productPriceTypeRepository,
        ProductPriceTypeQueryService productPriceTypeQueryService
    ) {
        this.productPriceTypeService = productPriceTypeService;
        this.productPriceTypeRepository = productPriceTypeRepository;
        this.productPriceTypeQueryService = productPriceTypeQueryService;
    }

    /**
     * {@code POST  /product-price-types} : Create a new productPriceType.
     *
     * @param productPriceType the productPriceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPriceType, or with status {@code 400 (Bad Request)} if the productPriceType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductPriceType> createProductPriceType(@Valid @RequestBody ProductPriceType productPriceType)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductPriceType : {}", productPriceType);
        if (productPriceType.getId() != null) {
            throw new BadRequestAlertException("A new productPriceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productPriceType = productPriceTypeService.save(productPriceType);
        return ResponseEntity.created(new URI("/api/product-price-types/" + productPriceType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productPriceType.getId().toString()))
            .body(productPriceType);
    }

    /**
     * {@code PUT  /product-price-types/:id} : Updates an existing productPriceType.
     *
     * @param id the id of the productPriceType to save.
     * @param productPriceType the productPriceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPriceType,
     * or with status {@code 400 (Bad Request)} if the productPriceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPriceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductPriceType> updateProductPriceType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductPriceType productPriceType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductPriceType : {}, {}", id, productPriceType);
        if (productPriceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPriceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productPriceType = productPriceTypeService.update(productPriceType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPriceType.getId().toString()))
            .body(productPriceType);
    }

    /**
     * {@code PATCH  /product-price-types/:id} : Partial updates given fields of an existing productPriceType, field will ignore if it is null
     *
     * @param id the id of the productPriceType to save.
     * @param productPriceType the productPriceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPriceType,
     * or with status {@code 400 (Bad Request)} if the productPriceType is not valid,
     * or with status {@code 404 (Not Found)} if the productPriceType is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPriceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPriceType> partialUpdateProductPriceType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductPriceType productPriceType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductPriceType : {}, {}", id, productPriceType);
        if (productPriceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPriceType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPriceType> result = productPriceTypeService.partialUpdate(productPriceType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productPriceType.getId().toString())
        );
    }

    /**
     * {@code GET  /product-price-types} : get all the Product Price Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Price Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductPriceType>> getAllProductPriceTypes(ProductPriceTypeCriteria criteria) {
        LOG.debug("REST request to get ProductPriceTypes by criteria: {}", criteria);

        List<ProductPriceType> entityList = productPriceTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /product-price-types/count} : count all the productPriceTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductPriceTypes(ProductPriceTypeCriteria criteria) {
        LOG.debug("REST request to count ProductPriceTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productPriceTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-price-types/:id} : get the "id" productPriceType.
     *
     * @param id the id of the productPriceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPriceType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductPriceType> getProductPriceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductPriceType : {}", id);
        Optional<ProductPriceType> productPriceType = productPriceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPriceType);
    }

    /**
     * {@code DELETE  /product-price-types/:id} : delete the "id" productPriceType.
     *
     * @param id the id of the productPriceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPriceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductPriceType : {}", id);
        productPriceTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
