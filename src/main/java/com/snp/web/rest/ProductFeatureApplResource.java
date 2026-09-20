package com.snp.web.rest;

import com.snp.domain.ProductFeatureAppl;
import com.snp.repository.ProductFeatureApplRepository;
import com.snp.service.ProductFeatureApplQueryService;
import com.snp.service.ProductFeatureApplService;
import com.snp.service.criteria.ProductFeatureApplCriteria;
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
 * REST controller for managing {@link com.snp.domain.ProductFeatureAppl}.
 */
@RestController
@RequestMapping("/api/product-feature-appls")
public class ProductFeatureApplResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFeatureApplResource.class);

    private static final String ENTITY_NAME = "productFeatureAppl";

    @Value("${jhipster.clientApp.name:snp}")
    private String applicationName;

    private final ProductFeatureApplService productFeatureApplService;

    private final ProductFeatureApplRepository productFeatureApplRepository;

    private final ProductFeatureApplQueryService productFeatureApplQueryService;

    public ProductFeatureApplResource(
        ProductFeatureApplService productFeatureApplService,
        ProductFeatureApplRepository productFeatureApplRepository,
        ProductFeatureApplQueryService productFeatureApplQueryService
    ) {
        this.productFeatureApplService = productFeatureApplService;
        this.productFeatureApplRepository = productFeatureApplRepository;
        this.productFeatureApplQueryService = productFeatureApplQueryService;
    }

    /**
     * {@code POST  /product-feature-appls} : Create a new productFeatureAppl.
     *
     * @param productFeatureAppl the productFeatureAppl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeatureAppl, or with status {@code 400 (Bad Request)} if the productFeatureAppl already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductFeatureAppl> createProductFeatureAppl(@Valid @RequestBody ProductFeatureAppl productFeatureAppl)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductFeatureAppl : {}", productFeatureAppl);
        if (productFeatureAppl.getId() != null) {
            throw new BadRequestAlertException("A new productFeatureAppl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productFeatureAppl = productFeatureApplService.save(productFeatureAppl);
        return ResponseEntity.created(new URI("/api/product-feature-appls/" + productFeatureAppl.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productFeatureAppl.getId().toString()))
            .body(productFeatureAppl);
    }

    /**
     * {@code PUT  /product-feature-appls/:id} : Updates an existing productFeatureAppl.
     *
     * @param id the id of the productFeatureAppl to save.
     * @param productFeatureAppl the productFeatureAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureAppl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductFeatureAppl> updateProductFeatureAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductFeatureAppl productFeatureAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductFeatureAppl : {}, {}", id, productFeatureAppl);
        if (productFeatureAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productFeatureAppl = productFeatureApplService.update(productFeatureAppl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureAppl.getId().toString()))
            .body(productFeatureAppl);
    }

    /**
     * {@code PATCH  /product-feature-appls/:id} : Partial updates given fields of an existing productFeatureAppl, field will ignore if it is null
     *
     * @param id the id of the productFeatureAppl to save.
     * @param productFeatureAppl the productFeatureAppl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeatureAppl,
     * or with status {@code 400 (Bad Request)} if the productFeatureAppl is not valid,
     * or with status {@code 404 (Not Found)} if the productFeatureAppl is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeatureAppl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeatureAppl> partialUpdateProductFeatureAppl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductFeatureAppl productFeatureAppl
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ProductFeatureAppl : {}, {}", id, productFeatureAppl);
        if (productFeatureAppl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeatureAppl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeatureApplRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeatureAppl> result = productFeatureApplService.partialUpdate(productFeatureAppl);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeatureAppl.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feature-appls} : get all the Product Feature Appls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Product Feature Appls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductFeatureAppl>> getAllProductFeatureAppls(
        ProductFeatureApplCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductFeatureAppls by criteria: {}", criteria);

        Page<ProductFeatureAppl> page = productFeatureApplQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-feature-appls/count} : count all the productFeatureAppls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductFeatureAppls(ProductFeatureApplCriteria criteria) {
        LOG.debug("REST request to count ProductFeatureAppls by criteria: {}", criteria);
        return ResponseEntity.ok().body(productFeatureApplQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-feature-appls/:id} : get the "id" productFeatureAppl.
     *
     * @param id the id of the productFeatureAppl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeatureAppl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureAppl> getProductFeatureAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductFeatureAppl : {}", id);
        Optional<ProductFeatureAppl> productFeatureAppl = productFeatureApplService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productFeatureAppl);
    }

    /**
     * {@code DELETE  /product-feature-appls/:id} : delete the "id" productFeatureAppl.
     *
     * @param id the id of the productFeatureAppl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFeatureAppl(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductFeatureAppl : {}", id);
        productFeatureApplService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
