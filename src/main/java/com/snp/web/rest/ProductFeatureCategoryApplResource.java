package com.snp.web.rest;

import com.snp.domain.ProductFeatureCategoryAppl;
import com.snp.repository.ProductFeatureCategoryApplRepository;
import com.snp.service.ProductFeatureCategoryApplQueryService;
import com.snp.service.ProductFeatureCategoryApplService;
import com.snp.service.criteria.ProductFeatureCategoryApplCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snp.domain.ProductFeatureCategoryAppl}.
 */
@RestController
@RequestMapping("/api/product-feature-category-appls")
public class ProductFeatureCategoryApplResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureCategoryApplResource.class);

    private static final String ENTITY_NAME = "productFeatureCategoryAppl";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureCategoryApplService productFeatureCategoryApplService;

    private final ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository;

    private final ProductFeatureCategoryApplQueryService productFeatureCategoryApplQueryService;

    public ProductFeatureCategoryApplResource(
        ProductFeatureCategoryApplService productFeatureCategoryApplService,
        ProductFeatureCategoryApplRepository productFeatureCategoryApplRepository,
        ProductFeatureCategoryApplQueryService productFeatureCategoryApplQueryService
    ) {
        this.productFeatureCategoryApplService = productFeatureCategoryApplService;
        this.productFeatureCategoryApplRepository = productFeatureCategoryApplRepository;
        this.productFeatureCategoryApplQueryService = productFeatureCategoryApplQueryService;
    }

    /**
     * {@code POST  /product-feature-category-appls} : Create a new productFeatureCategoryAppl.
     *
     * @param productFeatureCategoryAppl the productFeatureCategoryAppl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureCategoryAppl, or with status {@code 400 (Bad Request)} if the productFeatureCategoryAppl already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureCategoryAppl> createProductFeatureCategoryAppl(
        @Valid @RequestBody ProductFeatureCategoryAppl productFeatureCategoryAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureCategoryAppl : {}", productFeatureCategoryAppl);
        if (productFeatureCategoryAppl.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureCategoryAppl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureCategoryAppl = productFeatureCategoryApplService.save(productFeatureCategoryAppl);
        return ResponseEntity.created(new URI("/api/product-feature-category-appls/" + productFeatureCategoryAppl.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureCategoryAppl.getId().toString())
            )
            .body(productFeatureCategoryAppl);
    }

    /**
     * {@code PUT  /product-feature-category-appls/:id} : Updates an existing productFeatureCategoryAppl.
     *
     * @param id the id of the productFeatureCategoryAppl to save.
     * @param productFeatureCategoryAppl the productFeatureCategoryAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureCategoryAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureCategoryAppl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureCategoryAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureCategoryAppl> updateProductFeatureCategoryAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureCategoryAppl productFeatureCategoryAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureCategoryAppl : {}, {}", id, productFeatureCategoryAppl);
        if (productFeatureCategoryAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureCategoryAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureCategoryApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureCategoryAppl = productFeatureCategoryApplService.update(productFeatureCategoryAppl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureCategoryAppl.getId().toString()))
            .body(productFeatureCategoryAppl);
    }

    /**
     * {@code PATCH  /product-feature-category-appls/:id} : Partial updates given fields of an existing productFeatureCategoryAppl, field will ignore if it is null
     *
     * @param id the id of the productFeatureCategoryAppl to save.
     * @param productFeatureCategoryAppl the productFeatureCategoryAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureCategoryAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureCategoryAppl is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureCategoryAppl is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureCategoryAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureCategoryAppl> partialUpdateProductFeatureCategoryAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureCategoryAppl productFeatureCategoryAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureCategoryAppl : {}, {}", id, productFeatureCategoryAppl);
        if (productFeatureCategoryAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureCategoryAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureCategoryApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureCategoryAppl> result = productFeatureCategoryApplService.partialUpdate(productFeatureCategoryAppl);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureCategoryAppl.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-category-appls} : get all the Product Feature Category Appls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Category Appls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureCategoryAppl>> getAllProductFeatureCategoryAppls(
        ProductFeatureCategoryApplCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFeatureCategoryAppls by criteria: {}", criteria);

        Page<ProductFeatureCategoryAppl> page = productFeatureCategoryApplQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-feature-category-appls/count} : count all the productFeatureCategoryAppls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureCategoryAppls(ProductFeatureCategoryApplCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureCategoryAppls by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureCategoryApplQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-category-appls/:id} : get the "id" productFeatureCategoryAppl.
     *
     * @param id the id of the productFeatureCategoryAppl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureCategoryAppl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureCategoryAppl> getProductFeatureCategoryAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureCategoryAppl : {}", id);
        Optional<ProductFeatureCategoryAppl> productFeatureCategoryAppl = productFeatureCategoryApplService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureCategoryAppl);
    }

    /**
     * {@code DELETE  /product-feature-category-appls/:id} : delete the "id" productFeatureCategoryAppl.
     *
     * @param id the id of the productFeatureCategoryAppl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureCategoryAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureCategoryAppl : {}", id);
        productFeatureCategoryApplService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
